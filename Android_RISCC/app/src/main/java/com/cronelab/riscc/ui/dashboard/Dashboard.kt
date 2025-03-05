package com.cronelab.riscc.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.cronelab.riscc.BuildConfig
import com.cronelab.riscc.R
import com.cronelab.riscc.support.analytics.AnalyticsBroadcastManager
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModel
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.common.baseClass.BaseActivity
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.support.constants.*
import com.cronelab.riscc.support.constants.Constants.RequestCodes.CALL_PHONE
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.helper.BlurBuilder
import com.cronelab.riscc.support.helper.Coroutines
import com.cronelab.riscc.support.manager.LanguageManager
import com.cronelab.riscc.support.manager.UserManager
import com.cronelab.riscc.support.permissionManager.PermissionManager
import com.cronelab.riscc.support.services.InternetCheckerService
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.PdfViewer
import com.cronelab.riscc.ui.aboutUs.AboutUsViewModel
import com.cronelab.riscc.ui.aboutUs.AboutUsViewModelFactory
import com.cronelab.riscc.ui.auth.UpdateProfile
import com.cronelab.riscc.ui.auth.login.ChangePassword
import com.cronelab.riscc.ui.auth.login.Login
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.cronelab.riscc.ui.dashboard.viewModel.AppVersionFactory
import com.cronelab.riscc.ui.dashboard.viewModel.AppVersionViewModel
import com.cronelab.riscc.ui.feedback.Feedback
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import com.cronelab.riscc.ui.resources.ResourceViewModel
import com.cronelab.riscc.ui.resources.ResourceViewModelFactory
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.dialog_contact_us.*
import kotlinx.android.synthetic.main.menu_listview.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class Dashboard : BaseActivity(), KodeinAware/*, BottomNavigationView.OnNavigationItemSelectedListener*/ {

    val TAG = "Dashboard"
    override val kodein by kodein()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val userViewModelFactory: UserViewModelFactory by instance()
    private lateinit var userViewModel: UserViewModel
    private var dbUser: DBUser? = null

    private val appVersionVMFactory: AppVersionFactory by instance()
    private lateinit var appVersionVM: AppVersionViewModel

    private val aboutUserViewModelFactory: AboutUsViewModelFactory by instance()
    private lateinit var aboutUsViewModel: AboutUsViewModel

    private lateinit var resourcesViewModel: ResourceViewModel
    private val resourcesViewModelFactory: ResourceViewModelFactory by instance()

    private var progressDialog: Dialog? = null

    var phoneNumber = ""
    private val phoneCallPermission = Manifest.permission.CALL_PHONE
    private lateinit var permissionManager: PermissionManager


    private val internetStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!isFinishing) {
                val status = intent.getBooleanExtra("status", false)
                if (status) {
                    noInternetConnectionLayout.setVisibility(View.GONE)
                } else {
                    noInternetConnectionLayout.setVisibility(VISIBLE)
                }
            }
        }
    }

    private val updateProfileBroadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (!this@Dashboard.isFinishing) updateProfile()
        }
    }

    private fun updateProfile() {
        if (dbUser != null) {
            userViewModel.updateFcmToken(dbUser!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        configureNavigationController()

        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
        appVersionVM = ViewModelProviders.of(this, appVersionVMFactory).get(AppVersionViewModel::class.java)
        aboutUsViewModel = ViewModelProviders.of(this, aboutUserViewModelFactory).get(AboutUsViewModel::class.java)
        bottomNavigationView.setOnNavigationItemReselectedListener { }
        resourcesViewModel = ViewModelProviders.of(this, resourcesViewModelFactory).get(ResourceViewModel::class.java)
        resourcesViewModel.getResourceFiles()
        toolbarTitleTV.text = getString(R.string.survey)
        permissionManager = PermissionManager(this);
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        setUserData()
        startService(Intent(this, InternetCheckerService::class.java))
        LocalBroadcastManager.getInstance(this@Dashboard).registerReceiver(updateProfileBroadCastReceiver, IntentFilter(UPDATE_PROFILE_BROADCAST))
        LocalBroadcastManager.getInstance(this@Dashboard).registerReceiver(internetStatusReceiver, IntentFilter(INTERNET_CONNECTION_STATUS))
        if (menuListView.visibility == VISIBLE) {
            postAnalytics(MENU_IN)
        }
    }

    override fun onPause() {
        if (menuListView.visibility == VISIBLE) {
            postAnalytics(MENU_OUT)
        }
        super.onPause()
        LocalBroadcastManager.getInstance(this@Dashboard).unregisterReceiver(updateProfileBroadCastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (menuListView.visibility == VISIBLE) {
            postAnalytics(MENU_OUT)
        }
        LocalBroadcastManager.getInstance(this@Dashboard).unregisterReceiver(internetStatusReceiver)

    }

    private fun configureNavigationController() {
        /*appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_survey,
            R.id.navigation_notification,
            R.id.navigation_note,
            R.id.navigation_links,
            R.id.navigation_feedback
        ).build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)*/
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_survey -> {
                    toolbarTitleTV.text = getString(R.string.survey)
                }
                R.id.navigation_notification -> {
                    toolbarTitleTV.text = getString(R.string.notification)
                }
                R.id.navigation_note -> {
                    toolbarTitleTV.text = getString(R.string.note)
                }
                R.id.navigation_links -> {
                    toolbarTitleTV.text = getString(R.string.link)
                }
                R.id.navigation_answer -> {
                    toolbarTitleTV.text = getString(R.string.yourAnswer)
                }
            }
        }
    }


    fun openMenu(view: View) {
        AnalyticsBroadcastManager().sendPostAnalyticsBroadcast(MENU_IN)
        menuListView.visibility = VISIBLE
        menuListView.setBackgroundResource(0)
        val thread = Thread {
            try {
                Thread.sleep(100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            makeBlur()
        }
        thread.start()

    }

    @SuppressLint("SetTextI18n")
    private fun setUserData() {
        dbUser = User.getInstance(this).user
        dbUser?.let {
            val profileUrl = BuildConfig.SERVER_URL + it.imageUrl
            Glide.with(this).load(profileUrl).into(profileThumb)
            nameTV.text = "${it.firstName} ${it.lastName}"
            phoneTV.text = it.mobileNumber
            emailTV.text = it.emailAddress
            appVersionVM.getAppVersion(dbUser!!)
            aboutUsViewModel.getAboutUsData(dbUser!!)
        }
        /* userViewModel.getUser()
         userViewModel.user.observe(this, Observer {
             if (it.isNotEmpty()) {
                 dbUser = it[0]
                 dbUser?.let { user ->
                     val profileUrl = BASE_URL + user.imageUrl
                     Glide.with(this).load(profileUrl).into(profileThumb)
                     nameTV.text = user.firstName + " " + user.lastName
                     phoneTV.text = user.mobileNumber
                     emailTV.text = user.emailAddress
                     appVersionVM.getAppVersion(user)
                     aboutUsViewModel.getAboutUsData(user)
                 }

             } else {
                 showToast("No user found")
             }
         })*/
    }

    fun hideMenu(view: View) {
        menuListView.visibility = View.GONE
        postAnalytics(MENU_OUT)
    }

    fun navigateMenu(view: View) {
        menuListView.visibility = View.GONE
        postAnalytics(MENU_OUT)
        when (view.id) {
            R.id.surveyRootLayout -> {
                //val navigationView: View = bottomNavigationView.findViewById(R.id.navigation_survey)
                //navigationView.performClick()
                navController.navigate(R.id.navigation_survey)
            }

            R.id.notificationRootLayout -> {
                navController.navigate(R.id.navigation_notification)
            }
            R.id.noteRootLayout -> {
                navController.navigate(R.id.navigation_note)
            }
            R.id.LinkRootLayout -> {
                navController.navigate(R.id.navigation_links)
            }
            R.id.answerRootLayout -> {
                navController.navigate(R.id.navigation_answer)
            }
            R.id.feedbackRootLayout -> {
                startActivity(Intent(this@Dashboard, Feedback::class.java))
            }
            R.id.contactUsRootLayout -> {
                contactUsDialog.visibility = VISIBLE
            }
            R.id.privacyPolicyRootLayout -> {
                val intent = Intent(this@Dashboard, PdfViewer::class.java)
                intent.putExtra("Url", BuildConfig.SERVER_URL+URL_PRIVACY_POLICY)
//                if (LanguageManager.getLanguageCode().equals("en", ignoreCase = true)) {
//                    intent.putExtra("Url", BuildConfig.SERVER_URL+URL_PRIVACY_POLICY)
//                } else {
//                    intent.putExtra("Url", URL_PRIVACY_POLICY_SE)
//                }
                intent.putExtra("AboutUs",false)
                startActivity(intent)
            }
            R.id.aboutRootLayout -> {
                val intent = Intent(this@Dashboard, PdfViewer::class.java)
                intent.putExtra("AboutUs",true)
                startActivity(intent)
            }
            R.id.changePasswordRootLayout -> {
                startActivity(Intent(this@Dashboard, ChangePassword::class.java))
            }
            R.id.editProfileLayout -> {
                startActivity(Intent(this@Dashboard, UpdateProfile::class.java))

            }
            R.id.logoutLayout -> {
                showLogoutDialog()
            }

            R.id.logoutRootLayout -> {
                showLogoutDialog()
            }

            R.id.contactUsOkBtn -> {
                contactUsDialog.visibility = View.GONE
            }
            R.id.aboutUsPhoneTV -> {
                val alertDialogBuilder = AlertDialog.Builder(this)
                with(alertDialogBuilder) {
                    setTitle(phoneNumber)
                    setCancelable(false)
                    setPositiveButton(getString(R.string.call)) { dialog, which ->
                        checkPermission()
                        dialog.dismiss()
                    }
                    setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                        dialog.dismiss()
                    }
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
    }

    private fun logout() {
        dbUser?.let {
            progressDialog = ProgressDialog.loadingProgressDialog(this, "Logging out...")
            progressDialog?.show()
            userViewModel.logOut(it)
        }
    }

    private fun showLogoutDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this@Dashboard)
        with(alertDialogBuilder) {
            //title = getString(R.string.logout)+"?"
            setMessage(getString(R.string.logoutWarningMsg))
            setPositiveButton(getString(R.string.logout)) { dialog, which ->
                logout()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            show()
        }
    }

    override fun onBackPressed() {
        if (menuListView.visibility == VISIBLE) {
            menuListView.visibility = View.GONE
            postAnalytics(MENU_OUT)
        } else {
            super.onBackPressed()
        }
    }


    private fun makeBlur() {
        BlurBuilder.blur(this@Dashboard, getScreenShot(rootView), menuListView)
    }

    private fun getScreenShot(view: View): Bitmap? {
        val screenView = view.rootView
        screenView.isDrawingCacheEnabled = true
        val bitmap: Bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false
        return bitmap
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        appVersionVM.appVersionResponse.observe(this) {
            if (!this.isFinishing && it.error == null) {
                val version = it.appVersion[0].version + ""
                buildVersionTV.text = getString(R.string.buildVersion) + ": " + version
            }
        }

        aboutUsViewModel.aboutUs.observe(this) {
            if (!this.isFinishing && it.error == null) {
                val aboutUs = it.aboutUs[0]
                aboutUsNameTV.text = aboutUs.name
                aboutUsPhoneTV.text = aboutUs.phone
                aboutUsEmailTV.text = aboutUs.email
                phoneNumber = aboutUs.phone + ""
            }
        }

        userViewModel.logOutResponse.observe(this) {
            progressDialog?.dismiss()
            if (!this.isFinishing && it.error == null) {
                if (it.error == null) {
                    UserManager().removeUser(this@Dashboard)
                    Coroutines.io {
                        val status = AppDatabase.clearDatabase()
                        val intent = Intent(this@Dashboard, Login::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    showToast(it.message)
                }
            }
        }

    }

    private fun makePhoneCall(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            val callIntent = Intent(Intent.ACTION_CALL)
            val callPersonnel = "tel:$phoneNumber"
            callIntent.data = Uri.parse(callPersonnel)
            startActivity(callIntent)
        } else {
            showToast(getString(R.string.invalidPhone))
        }

    }

    private fun checkPermission() {
        permissionManager.checkPermission(this, phoneCallPermission, object : PermissionManager.PermissionAskListener {
            override fun onNeedPermission() {
                ActivityCompat.requestPermissions(this@Dashboard, arrayOf(phoneCallPermission), CALL_PHONE)
            }

            override fun onPermissionPreviouslyDenied() {
                showRational(getString(R.string.permissionDenied), getString(R.string.phoneCallPermissionWarningPrompt))
            }

            override fun onPermissionPreviouslyDeniedWithNeverAskAgain() {
                dialogForSettings(getString(R.string.permissionDenied), getString(R.string.phoneCallPermissionPrompt))
            }

            override fun onPermissionGranted() {
                makePhoneCall(phoneNumber)
            }
        })
    }

    private fun showRational(title: String, message: String) {
        android.app.AlertDialog.Builder(this).setTitle(title).setMessage(message)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.iAmSure)) { dialog, which -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.retry)) { dialog, which ->
                ActivityCompat.requestPermissions(this@Dashboard, arrayOf(phoneCallPermission), CALL_PHONE)
                dialog.dismiss()
            }.show()
    }

    private fun dialogForSettings(title: String, msg: String) {
        android.app.AlertDialog.Builder(this).setTitle(title).setMessage(msg)
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
            Constants.RequestCodes.CALL_PHONE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //checkPermission()
                    makePhoneCall(phoneNumber)
                } else {
                    Toast.makeText(this, getString(R.string.pleaseGrantAllRequestedPermission), Toast.LENGTH_SHORT).show()
                    //ActivityCompat.requestPermissions(this@SignUp, this.permissions, PERMISSION_REQUEST_CODE)
                }
            }
        }
    }



    private fun postAnalytics(key: String) {
        val appAnalyticsViewModelFactory: AppAnalyticsViewModelFactory by instance()
        val appAnalyticsViewModel: AppAnalyticsViewModel = ViewModelProviders.of(this, appAnalyticsViewModelFactory).get(AppAnalyticsViewModel::class.java)
        appAnalyticsViewModel.getAppAnalytics(key)
        appAnalyticsViewModel.appAnalyticsType.observe(this) {
            if (it != null && it.isNotEmpty()) {
                appAnalyticsViewModel.resetObserver()
                appAnalyticsViewModel.appAnalyticsType.removeObserver { }
                if (dbUser != null) {
                    appAnalyticsViewModel.sendAppAnalytics(it[0], dbUser!!)
                }
            }
        }
    }

}