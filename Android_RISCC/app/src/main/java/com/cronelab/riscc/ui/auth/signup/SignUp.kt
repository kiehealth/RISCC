package com.cronelab.riscc.ui.auth.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.cronelab.riscc.BuildConfig
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.manager.ImageManager
import com.cronelab.riscc.support.common.utils.Utils
import com.cronelab.riscc.support.common.validation.Validation
import com.cronelab.riscc.support.constants.Constants
import com.cronelab.riscc.support.constants.URL_PRIVACY_POLICY
import com.cronelab.riscc.support.constants.URL_PRIVACY_POLICY_SE
import com.cronelab.riscc.support.data.database.table.DBResourceFile
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.manager.LanguageManager
import com.cronelab.riscc.support.permissionManager.PermissionManager
import com.cronelab.riscc.support.permissionManager.PermissionManager.PermissionAskListener
import com.cronelab.riscc.ui.WebViewer
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.cronelab.riscc.ui.auth.pojo.Verification
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import com.cronelab.riscc.ui.resources.ResourceViewModel
import com.cronelab.riscc.ui.resources.ResourceViewModelFactory
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.consent_view.*
import kotlinx.android.synthetic.main.signup_input_fields.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class SignUp : AppCompatActivity(), KodeinAware {

    val TAG = "SignUp"
    override val kodein by kodein()

    private val requestWriteExternalStorage = 10012
    private val writeExternalStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private lateinit var permissionManager: PermissionManager

    private var imageUri: Uri? = null
    private var profileImageBitmap: Bitmap? = null

    private lateinit var userViewModel: UserViewModel
    private val userViewModelFactory: UserViewModelFactory by instance()

    private lateinit var resourcesViewModel: ResourceViewModel
    private val resourcesViewModelFactory: ResourceViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
        inputLayout.visibility = View.GONE
        Glide.with(this).load(R.drawable.ic_user_placeholder).into(profileIV)
        initListeners()
        resourcesViewModel = ViewModelProviders.of(this, resourcesViewModelFactory).get(ResourceViewModel::class.java)
        resourcesViewModel.getResourceFilesFromDB()
        permissionManager = PermissionManager(this);
        initObservers()
    }

    private fun initListeners() {
        consentCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                consentLayout.visibility = View.GONE
                inputLayout.visibility = View.VISIBLE
            } else {
                consentLayout.visibility = View.VISIBLE
                inputLayout.visibility = View.GONE
            }
        }

        changeProfileIV.setOnClickListener {
            checkPermission() //check permission and open media selection dialog
        }

    }

    private fun initObservers() {
        resourcesViewModel.resourcesList.observe(this) {
            if (!this.isFinishing && it.isNotEmpty()) {
                val languageCode = Locale.getDefault().language
                if (languageCode.equals("SE", ignoreCase = true) || languageCode.equals(
                        "SV",
                        ignoreCase = true
                    )
                ) {
                    it.forEach { dbResourceFile ->
                        if (dbResourceFile.title.equals("CONSENT_SE")) {
                            showMultiLanguageConsent(dbResourceFile)
                        }
                    }
                } else if (languageCode.equals("ES", ignoreCase = true)) {
                    it.forEach { file ->
                        if (file.title.equals("CONSENT_ES")) {
                            showMultiLanguageConsent(file)
                        }
                    }
                } else if (languageCode.equals("NE", ignoreCase = true)) {
                    it.forEach { file ->
                        if (file.title.equals("CONSENT_SE")) {
                            showMultiLanguageConsent(file)
                        }
                    }
                } else {
                    it.forEach { file ->
                        if (file.title.equals("CONSENT")) {
                            showMultiLanguageConsent(file)
                        }
                    }
                }
            }
        }
    }

    private fun showMultiLanguageConsent(dbResourceFile: DBResourceFile) {
        val url = dbResourceFile.url
        url?.let { it ->
            loadPdf(BuildConfig.SERVER_URL + it)
        }
        consentDescriptionTV.text = dbResourceFile.description.orEmpty()
    }

    /*  private fun showMultiLanguageConsent(list: List<DBResourceFile>) {
          if (list.isNotEmpty()) {
              val url = list[1].url
              url?.let { url1 ->
                  loadPdf(BuildConfig.SERVER_URL + url1)
              }
          }
      }*/

    fun navigateToLogin(view: View) {
        if (Utils.isKeyboardShown(this@SignUp, view)) {
            Utils.hideKeyboardFrom(this@SignUp, view)
        }
        onBackPressed()
    }

    fun showImageSelectionDialog(/*view: View*/) {
        val uploadMediaDialog = Dialog(this@SignUp)
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
            startActivityForResult(Intent.createChooser(intent, getString(R.string.selectPicture)), Constants.RequestCodes.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE)
            uploadMediaDialog.dismiss()
        }
    }

    fun doSignUp(view: View) {
        if (termsOfServiceCB.isChecked) {
            val validation = validateData()
            if (validation.status) {
                val user = DBUser()
                user.firstName = firstNameET.text.toString()
                user.lastName = lastnameET.text.toString()
                user.emailAddress = emailET.text.toString()
                user.mobileNumber = phoneET.text.toString()
                user.password = passwordET.text.toString()
                if (profileImageBitmap != null) {
                    user.imageEncoded = ImageManager().encodeTobBase64(profileImageBitmap!!)
                } else {
                    val drawable = profileIV.drawable as BitmapDrawable
                    val bitmap = drawable.bitmap
                    user.imageEncoded = ImageManager().encodeTobBase64(bitmap)
                }

                val progressDialog = ProgressDialog.loadingProgressDialog(this, getString(R.string.signing_up))
                progressDialog.show()
                userViewModel.getVerificationCode(user)
                userViewModel.verificationCodeResponse.observe(this, Observer {
                    if (!this.isFinishing) {
                        progressDialog.dismiss()
                        userViewModel.verificationCodeResponse.removeObserver {}
                        if (it.error == null) {
                            showVerificationDialog(it.verification, user)
                        } else {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })

            } else {
                Toast.makeText(this, validation.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please check terms of service", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showVerificationDialog(verification: Verification, user: DBUser) {
        val layoutInflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = layoutInflater.inflate(R.layout.register_enter_verification_code, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        popupWindow.isOutsideTouchable = false
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val codeField1 = popupView.findViewById<EditText>(R.id.codeField1)
        val codeField2 = popupView.findViewById<EditText>(R.id.codeField2)
        val codeField3 = popupView.findViewById<EditText>(R.id.codeField3)
        val codeField4 = popupView.findViewById<EditText>(R.id.codeField4)
        initializeTextChangeListener(codeField1, codeField2, codeField3, codeField4)

        val phoneTV = popupView.findViewById<TextView>(R.id.phoneTV)
        phoneTV.text = phoneET.text.toString()

        val verifyBtn = popupView.findViewById<Button>(R.id.verifiyBtn)
        popupWindow.showAsDropDown(firstNameET)

        verifyBtn.setOnClickListener {
            val code = codeField1.text.toString() + codeField2.text.toString() + codeField3.text.toString() + codeField4.text.toString()
            if (verification.verificationCode.equals(code)) {
                val progressDialog = ProgressDialog.loadingProgressDialog(this, getString(R.string.verifyingAndSigningUp))
                progressDialog.show()
                userViewModel.signUp(user, verification)
                userViewModel.signUpResponse.observe(this, Observer {
                    if (!this.isFinishing) {
                        progressDialog.dismiss()
                        popupWindow.dismiss()
                        if (it.error != null) {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, getString(R.string.signUpSuccessfully), Toast.LENGTH_SHORT).show()
                        }
                        finish()
                    }
                })

            } else {
                Toast.makeText(this@SignUp, getString(R.string.verificationCodeDoesnotMatch), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeTextChangeListener(codeField1: EditText, codeField2: EditText, codeField3: EditText, codeField4: EditText) {
        codeField1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (codeField1.text.toString().isNotEmpty()) {
                    codeField2.isFocusable = true
                    codeField2.requestFocus()
                    codeField2.isCursorVisible = true
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        codeField2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (codeField2.text.toString().length > 0) {
                    codeField3.isFocusable = true
                    codeField3.requestFocus()
                    codeField3.isCursorVisible = true
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        codeField3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (codeField3.text.toString().length > 0) {
                    codeField4.isFocusable = true
                    codeField4.requestFocus()
                    codeField4.isCursorVisible = true
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        codeField4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (codeField4.getText().toString().length() > 0) {
//                    codeField5.setFocusable(true);
//                    codeField5.requestFocus();
//                    codeField5.setCursorVisible(true);
//                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


    private fun validateData(): Validation {
        return if (emailET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.emailEmpty), false)
        } else if (!Utils.isValidEmail(emailET.text.toString().trim())) {
            Validation(getString(R.string.invalidEmail), false)
        } else if (passwordET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.passwordEmpty), false)
        } else if (confirmPasswordET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.confirmPasswordEmpty), false)
        } else if (!passwordET.text.toString().equals(confirmPasswordET.text.toString().trim(), ignoreCase = true)) {
            Validation(getString(R.string.passwordDoesNotMatch), false)
        } else Validation("Success.", true)


        /*if (firstNameET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.firstNameEmpty), false)
        } else if (lastnameET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.lastnameEmpty), false)
        } else if (phoneET.text.toString().trim().isEmpty()) {
            Validation(getString(R.string.phoneEmpty), false)
        } else if (!Utils.isValidPhoneNumber(phoneET.text.toString().trim())) {
            Validation(getString(R.string.invalidPhone), false)
        } else if (profileImageBitmap == null) {
            Validation(getString(R.string.profileImageIsEmpty), false)
        }*/
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, Constants.RequestCodes.CAPTURE_IMAGE_REQUEST_CODE)
    }


    private fun loadPdf(url: String) {
        /* val progressDialog = ProgressDialog.loadingProgressDialog(this@SignUp, resources.getString(R.string.loadingConcent))
         progressDialog.show()*/
        @SuppressLint("HandlerLeak") val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                //progressDialog.dismiss()
                val bundle = msg.data
                if (bundle.getString("error").equals("0", ignoreCase = true)) { // no error
                    if (bundle.getString("Content-Type").equals("application/pdf", ignoreCase = true)) {
                        showPDF(bundle.getString("URI"))
                    }
                } else {
                    //noPreviewAvailableTV.visibility = View.VISIBLE
                }
            }
        }
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    val name = System.currentTimeMillis().toString()
                    val pdfFileName = "$name.pdf"
                    val imageFileName = "$name.jpeg"
                    val ulrn = URL(url)
                    Log.d("D2R", ulrn.toString())
                    val con = ulrn.openConnection() as HttpURLConnection
                    con.requestMethod = "GET"

                    val contentType = con.getHeaderField("Content-Type")
                    var sourceFile: File? = null
                    if (contentType.equals("application/pdf", ignoreCase = true)) {
                        sourceFile = File("$cacheDir/$pdfFileName")
                    } else if (contentType.equals("image/jpeg", ignoreCase = true) || contentType.equals("image/png", ignoreCase = true)) {
                        sourceFile = File("$cacheDir/$imageFileName")
                    }
                    val inputStream: InputStream = BufferedInputStream(con.inputStream)
                    Log.e("content Type", con.getHeaderField("Content-Type"))
                    val fos = FileOutputStream(sourceFile!!)
                    val buffer = ByteArray(1024)
                    var len1: Int
                    while (inputStream.read(buffer).also { len1 = it } > 0) {
                        fos.write(buffer, 0, len1)
                    }
                    fos.close()
                    val f = File(sourceFile.path)
                    if (!f.exists()) {
                        Log.e(TAG, "File '" + f.absolutePath + "' not found")
                        throw FileNotFoundException("The file does not exists")
                    }
                    val len = f.length()
                    if (len == 0L) {
                        Log.e(TAG, "File '" + f.absolutePath + "' has no length")
                        throw IllegalArgumentException("File has no length")
                    }
                    val msg = handler.obtainMessage()
                    val bundle = Bundle()
                    bundle.putString("URI", f.absolutePath)
                    bundle.putString("Content-Type", contentType)
                    msg.data = bundle
                    bundle.putString("error", "0")
                    Log.e("bundle", f.absolutePath)
                    handler.sendMessage(msg)
                } catch (e: Exception) {
                    // progressDialog.dismiss()
                    Log.e(TAG, "Exception while writing to device! " + e.message)

                    //sending error msg
                    val msg = handler.obtainMessage()
                    val bundle = Bundle()
                    bundle.putString("error", "1")
                    msg.data = bundle
                    handler.sendMessage(msg)
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    private fun showPDF(pdfPath: String?) {
        pdfPath?.let {
            pdfViewer.fromFile(File(pdfPath)).enableSwipe(true).swipeHorizontal(false).enableDoubletap(true).defaultPage(0).enableAnnotationRendering(false).password(null).scrollHandle(null).enableAntialiasing(true).spacing(0).load()
        }
    }

    private fun checkPermission() {
        permissionManager.checkPermission(this, writeExternalStoragePermission, object : PermissionAskListener {
            override fun onNeedPermission() {
                ActivityCompat.requestPermissions(this@SignUp, arrayOf(writeExternalStoragePermission), requestWriteExternalStorage)
            }

            override fun onPermissionPreviouslyDenied() {
                showRational(getString(R.string.permissionDenied), getString(R.string.storagePermissionNotAllowedWarningmsg))
            }

            override fun onPermissionPreviouslyDeniedWithNeverAskAgain() {
                dialogForSettings(getString(R.string.permissionDenied), getString(R.string.storageAccessMsgPrompt))
            }

            override fun onPermissionGranted() {
                showImageSelectionDialog()
            }
        })
    }

    private fun showRational(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.iAmSure)) { dialog, which -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.retry)) { dialog, which ->
                ActivityCompat.requestPermissions(this@SignUp, arrayOf(writeExternalStoragePermission), requestWriteExternalStorage)
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
            requestWriteExternalStorage -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //checkPermission()
                    showImageSelectionDialog()
                } else {
                    Toast.makeText(this, getString(R.string.pleaseGrantAllRequestedPermission), Toast.LENGTH_SHORT).show()
                    //ActivityCompat.requestPermissions(this@SignUp, this.permissions, PERMISSION_REQUEST_CODE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RequestCodes.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            try {
                profileImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                Glide.with(this@SignUp).load(profileImageBitmap).into(profileIV)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (requestCode == Constants.RequestCodes.CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                profileImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                Glide.with(this@SignUp).load(profileImageBitmap).into(profileIV)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun showTermsOfService(view: View) {
        /*  val intent = Intent(this@SignUp, WebViewer::class.java)
          intent.putExtra("Url", URL_PRIVACY_POLICY)
          startActivity(intent)*/

        val intent = Intent(this@SignUp, WebViewer::class.java)
        intent.putExtra("Url", BuildConfig.SERVER_URL+URL_PRIVACY_POLICY)
        startActivity(intent)

//        if (LanguageManager.getLanguageCode().equals("en", ignoreCase = true)) {
//            intent.putExtra("Url", URL_PRIVACY_POLICY)
//        } else {
//            intent.putExtra("Url", URL_PRIVACY_POLICY_SE)
//        }
    }


}