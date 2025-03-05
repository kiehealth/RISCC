package com.cronelab.riscc.ui.auth.login

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.R
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModel
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.ui.dashboard.Dashboard
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import com.cronelab.riscc.support.common.utils.Utils
import com.cronelab.riscc.support.manager.UserManager
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.auth.ForgotPassword
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.cronelab.riscc.ui.auth.signup.SignUp
import com.cronelab.riscc.ui.resources.ResourceViewModel
import com.cronelab.riscc.ui.resources.ResourceViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.emailET
import kotlinx.android.synthetic.main.activity_login.passwordET
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class Login : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private lateinit var userViewModel: UserViewModel
    private val userFactory: UserViewModelFactory by instance()

    private lateinit var resourcesViewModel: ResourceViewModel
    private val resourcesViewModelFactory: ResourceViewModelFactory by instance()

    private lateinit var appAnalyticsViewModel: AppAnalyticsViewModel
    private val appAppAnalyticsViewModelFactory: AppAnalyticsViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initUI()
        userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java)
        appAnalyticsViewModel = ViewModelProviders.of(this, appAppAnalyticsViewModelFactory).get(AppAnalyticsViewModel::class.java)
        resourcesViewModel = ViewModelProviders.of(this, resourcesViewModelFactory).get(ResourceViewModel::class.java)
        resourcesViewModel.getResourceFiles()
        initObservers()

        //emailET.setText("android.tester@yopmail.com")
        // passwordET.setText("androidTester")
    }

    private fun initObservers() {
        /* resourcesViewModel.resourceData.observe(this, {
             if (!this.isFinishing && it.error == null) {
                 showToast(it.message + "")
             }
         })*/
    }

    private fun initUI() {
        emailET.addTextChangedListener(emailTextWatcher)
        passwordET.addTextChangedListener(passwordTextWatcher)
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty() && !passwordET.text.isNullOrEmpty()) {
                configureView(signInBtnParent, ContextCompat.getColor(this@Login, R.color.colorPrimary))
                signInBtnParent.isEnabled = true
            } else {
                configureView(signInBtnParent, ContextCompat.getColor(this@Login, R.color.lt_grey))
                signInBtnParent.isEnabled = false
            }
        }

    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty() && !emailET.text.isNullOrEmpty()) {
                configureView(signInBtnParent, ContextCompat.getColor(this@Login, R.color.colorPrimary))
                signInBtnParent.isEnabled = true
            } else {
                configureView(signInBtnParent, ContextCompat.getColor(this@Login, R.color.lt_grey))
                signInBtnParent.isEnabled = false
            }
        }
    }

    private fun configureView(view: View, colorCode: Int) {
        if (view is LinearLayout) {
            val drawable = view.background as GradientDrawable
            drawable.setColor(colorCode)
        }
    }

    fun doLogin(view: View) {
        if (validateData(emailET.text.toString(), passwordET.text.toString())) {
            val progressDialog = ProgressDialog.loadingProgressDialog(this, getString(R.string.logging_in_msg))
            progressDialog.show()
            userViewModel.loginResponse.observe(this@Login, Observer {
                it ?: return@Observer
                if (!this.isFinishing) {
                    progressDialog.dismiss()
                    //userViewModel.loginResponse.removeObserver {}
                    if (it.error != null) {
                        showToast(it.message)
                    } else {
                        UserManager().saveUser(this, it.user)
                        User.getInstance(this).user = it.user
                        UserManager().saveAuth(emailET.text.toString(), passwordET.text.toString())
                        appAnalyticsViewModel.getAppAnalytics(it.user)
                        val intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)
                        this.finish()
                    }
                }
            })
            userViewModel.login(emailET.text.toString(), passwordET.text.toString())
        }
    }

    private fun validateData(email: String?, password: String?): Boolean {
        return if (email.isNullOrEmpty() && password.isNullOrEmpty()) {
            showToast(getString(R.string.emailEmpty))
            false
        } else if (!Utils.isValidEmail(emailET.text.toString().trim())) {
            showToast(getString(R.string.invalidEmail))
            false
        } else if (password.isNullOrEmpty()) {
            showToast(getString(R.string.passwordEmpty))
            false
        } else {
            true
        }
    }

    fun navigateToSignUp(view: View) {
        startActivity(Intent(Login@ this, SignUp::class.java))
    }

    fun navigateToForgotPassword(view: View) {
        startActivity(Intent(Login@ this, ForgotPassword::class.java))
    }


}