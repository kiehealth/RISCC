package com.cronelab.riscc.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.ViewUtils
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.ui.auth.login.Login
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ResetPassword : AppCompatActivity() , KodeinAware {

    override val kodein by kodein()

    private var emailAddress = ""

    private lateinit var userViewModel: UserViewModel
    private val userViewModelFactory: UserViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
        // initUI()
        emailAddress = intent.getStringExtra("email").toString()
    }

    private fun initUI() {
        verificationCodeET.addTextChangedListener(verificationTextWatcher)
        newPasswordET.addTextChangedListener(newPasswordTextWatcher)
        confirmNewPasswordET.addTextChangedListener(confirmPasswordTextWatcher)
    }

    fun resetPassword(view: View) {
        val progressDialog = ProgressDialog.loadingProgressDialog(this, getString(R.string.resettingPassword))
        progressDialog.show()
        userViewModel.resetPassword(emailAddress, newPasswordET.text.toString(), verificationCodeET.text.toString())
        userViewModel.passwordResetResponse.observe(this, {
            if (!this.isFinishing) {
                progressDialog.dismiss()
                showToast(it.message)
                if (it.error == null) {
                    startActivity(Intent(this@ResetPassword, Login::class.java))
                    finish()
                }
            }
        })
    }

    private val verificationTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty() && newPasswordET.text.toString().isNotEmpty() && confirmNewPasswordET.text.toString().isNotEmpty()) {
                ViewUtils.setBgColor(resetPasswordBtn, ContextCompat.getColor(this@ResetPassword, R.color.colorPrimary))
                resetPasswordBtn.isEnabled = true
            } else {
                ViewUtils.setBgColor(sendVerificationCodeBtn, ContextCompat.getColor(this@ResetPassword, R.color.grey))
                resetPasswordBtn.isEnabled = false
            }
        }

    }
    private val newPasswordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty() && verificationCodeET.text.toString().isNotEmpty() && confirmNewPasswordET.text.toString().isNotEmpty()) {
                ViewUtils.setBgColor(resetPasswordBtn, ContextCompat.getColor(this@ResetPassword, R.color.colorPrimary))
                resetPasswordBtn.isEnabled = true
            } else {
                ViewUtils.setBgColor(sendVerificationCodeBtn, ContextCompat.getColor(this@ResetPassword, R.color.grey))
                resetPasswordBtn.isEnabled = false
            }
        }

    }
    private val confirmPasswordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty() && newPasswordET.text.toString().isNotEmpty() && verificationCodeET.text.toString().isNotEmpty()) {
                ViewUtils.setBgColor(resetPasswordBtn, ContextCompat.getColor(this@ResetPassword, R.color.colorPrimary))
                resetPasswordBtn.isEnabled = true
            } else {
                ViewUtils.setBgColor(sendVerificationCodeBtn, ContextCompat.getColor(this@ResetPassword, R.color.grey))
                resetPasswordBtn.isEnabled = false
            }
        }

    }
}