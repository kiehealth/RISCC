package com.cronelab.riscc.ui.auth

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.manager.ImageManager
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.support.common.utils.Utils
import com.cronelab.riscc.support.common.validation.Validation
import com.cronelab.riscc.support.constants.Constants
import com.cronelab.riscc.support.constants.Constants.RequestCodes.WRITE_STORAGE_ACCESS
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.permissionManager.PermissionManager
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.cronelab.riscc.ui.dashboard.Dashboard
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.activity_update_profile.emailET
import kotlinx.android.synthetic.main.activity_update_profile.firstNameET
import kotlinx.android.synthetic.main.activity_update_profile.lastnameET
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.IOException

class UpdateProfile : AppCompatActivity(), KodeinAware {

    private var TAG = "UpdateProfile"

    override val kodein by kodein()

    private val userViewModelFactory: UserViewModelFactory by instance()
    private lateinit var userViewModel: UserViewModel
    var user: DBUser? = null

    private var imageUri: Uri? = null
    private var profileImageBitmap: Bitmap? = null

    private val writeExternalStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
        permissionManager = PermissionManager(this);
        getUser()
    }

    private fun getUser() {
        userViewModel.getUser()
        userViewModel.user.observe(this) {
            if (it.isNotEmpty()) {
                userViewModel.user.removeObserver { }
                user = it[0]
                user?.let {
                    firstNameET.setText(it.firstName)
                    lastnameET.setText(it.lastName)
                    emailET.setText(it.emailAddress)
                    phoneNumberET.setText(it.mobileNumber)
                    Glide.with(this).load(it.imageUrl).into(updateProfileIV)
                }
            }
        }
    }

    fun showMediaSelectionDialog() {
        val uploadMediaDialog = Dialog(this)
        uploadMediaDialog.setContentView(R.layout.upload_picture_dialog)
        uploadMediaDialog.show()
        val takePhotoBtn = uploadMediaDialog.findViewById<Button>(R.id.takePhotoBtn)
        val selectFromGalleryBtn = uploadMediaDialog.findViewById<Button>(R.id.selectFromGalleryBtn)
        takePhotoBtn.setOnClickListener {
            openCamera()
            uploadMediaDialog.dismiss()
        }
        selectFromGalleryBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.RequestCodes.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE)
            uploadMediaDialog.dismiss()
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, Constants.RequestCodes.CAPTURE_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "got activity result")
        if (requestCode == Constants.RequestCodes.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            try {
                profileImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                Glide.with(this).load(profileImageBitmap).into(updateProfileIV)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(TAG, "gallery image pick ex: " + e.message)
            }
        }
        if (requestCode == Constants.RequestCodes.CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                Log.d(TAG, "Got Image from Camera : image uri: $imageUri")
                profileImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                if (Build.MANUFACTURER.equals("samsung", ignoreCase = true)) {
                    profileImageBitmap?.let {
                        profileImageBitmap = ImageManager().rotateBitmap(it, 90f)
                    }
                }
                Glide.with(this).load(profileImageBitmap).into(updateProfileIV)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, e.message!!)
            }
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(this.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun updateProfile(view: View) {
        val validation = validateData()
        if (validation.status) {
            try {
                user?.let { user ->
                    user.firstName = firstNameET.text.toString()
                    user.lastName = lastnameET.text.toString()
                    user.mobileNumber = phoneNumberET.text.toString()
                    user.emailAddress = emailET.text.toString()
                    if (profileImageBitmap == null) {
                        profileImageBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_user_placeholder)
                    }
                    try {
                        imageUri = getImageUri((updateProfileIV.drawable as BitmapDrawable).bitmap)
                        if (imageUri != null) {
                            profileImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    user.imageEncoded = ImageManager().encodeTobBase64(profileImageBitmap!!)
                    userViewModel.updateProfile(user)
                    val progressDialog = ProgressDialog.loadingProgressDialog(this, getString(R.string.updatingProfile))
                    progressDialog.show()
                    userViewModel.updateProfileResponse.observe(this) {
                        if (!this.isFinishing) {
                            progressDialog.dismiss()

                            showToast(it.message)
                            if (it.error == null) {
                                getUser()

                                startActivity(Intent(this@UpdateProfile, Dashboard::class.java))
                                finish()
                            }
                        }
                    }
                }


            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            showToast(validation.message)
        }
    }

    private fun validateData(): Validation {
        return if (firstNameET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.firstNameEmpty), false)
        } else if (lastnameET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.lastnameEmpty), false)
        } else if (emailET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.emailEmpty), false)
        } else if (!Utils.isValidEmail(emailET.text.toString().trim())) {
            Validation(getString(R.string.invalidEmail), false)
        } else if (phoneNumberET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.phoneEmpty), false)
        } else if (!Utils.isValidPhoneNumber(phoneNumberET.text.toString().trim())) {
            Validation(getString(R.string.invalidPhone), false)
        } else if (profileImageBitmap == null) {
            Validation(getString(R.string.profileImageIsEmpty), false)
        } else Validation("Success.", true)

    }

    fun navigateBack(view: View) {
        finish()
    }

    fun removeImage(view: View) {
        Glide.with(this).load(R.drawable.ic_user_thumb).into(updateProfileIV)
    }



    fun checkPermission(view: View) {
        permissionManager.checkPermission(this, writeExternalStoragePermission, object : PermissionManager.PermissionAskListener {
            override fun onNeedPermission() {
                ActivityCompat.requestPermissions(this@UpdateProfile, arrayOf(writeExternalStoragePermission), WRITE_STORAGE_ACCESS)
            }

            override fun onPermissionPreviouslyDenied() {
                showRational(getString(R.string.permissionDenied), getString(R.string.storagePermissionNotAllowedWarningmsg))
            }

            override fun onPermissionPreviouslyDeniedWithNeverAskAgain() {
                dialogForSettings(getString(R.string.permissionDenied), getString(R.string.storageAccessMsgPrompt))
            }

            override fun onPermissionGranted() {
                showMediaSelectionDialog()
            }
        })
    }

    private fun showRational(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.iAmSure)) { dialog, which -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.retry)) { dialog, which ->
                ActivityCompat.requestPermissions(this@UpdateProfile, arrayOf(writeExternalStoragePermission), WRITE_STORAGE_ACCESS)
                dialog.dismiss()
            }.show()
    }

    private fun dialogForSettings(title: String, msg: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(msg)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.notNow)) { dialog, which -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.settings)) { dialog, which ->
                goToSettings()
                dialog.dismiss()
            }.show()
    }

    private fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.parse("package:$packageName")
        intent.data = uri
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_STORAGE_ACCESS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //checkPermission()
                    showMediaSelectionDialog()
                } else {
                    Toast.makeText(this, getString(R.string.pleaseGrantAllRequestedPermission), Toast.LENGTH_SHORT).show()
                    //ActivityCompat.requestPermissions(this@SignUp, this.permissions, PERMISSION_REQUEST_CODE)
                }
            }
        }
    }



}