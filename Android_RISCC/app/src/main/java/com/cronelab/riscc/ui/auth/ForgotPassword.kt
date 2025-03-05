package com.cronelab.riscc.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.ViewUtils.setBgColor
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.support.common.utils.Utils
import com.cronelab.riscc.support.common.validation.Validation
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ForgotPassword : AppCompatActivity(), KodeinAware {
    private val TAG = "ForgotPassword"

    override val kodein by kodein()

    private lateinit var userViewModel: UserViewModel
    private val userViewModelFactory: UserViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        initUI()
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
    }

    private fun initUI() {
        setBgColor(sendVerificationCodeBtn, ContextCompat.getColor(this@ForgotPassword, R.color.grey))
        sendVerificationCodeBtn.isEnabled = false
        emailET.addTextChangedListener(emailTextWatcher)
    }

    fun navigateBack(view: View) {
        onBackPressed()
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty()) {
                setBgColor(sendVerificationCodeBtn, ContextCompat.getColor(this@ForgotPassword, R.color.colorPrimary))
                sendVerificationCodeBtn.isEnabled = true
            } else {
                setBgColor(sendVerificationCodeBtn, ContextCompat.getColor(this@ForgotPassword, R.color.grey))
                sendVerificationCodeBtn.isEnabled = false
            }
        }

    }

    private fun resetPassword() {
        var email = emailET.text.toString().trim()
        if (!Utils.isValidEmail(email)) {
            if (!email.startsWith("+")) {
                email = "%2B" + emailET.text.toString()
            } else {
                email = email.replace("+", "%2B")
            }
        }

        val progressDialog = ProgressDialog.loadingProgressDialog(this, getString(R.string.requestVerificationcode))
        progressDialog.show()
        userViewModel.getPasswordResetCode(emailET.text.toString())
        userViewModel.passwordResetCodeResponse.observe(this, {
            if (!this.isFinishing) {
                progressDialog.dismiss()
                showToast(it.message)
                if (it.error == null) {
                    val intent = Intent(this@ForgotPassword, ResetPassword::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    fun sendResetVerificationCode(view: View) {
        val validation: Validation = dataValidation()
        if (validation.status) {
            //showProgressDialog(getString(R.string.requestVerificationcode))
            resetPassword()
        } else {
            Toast.makeText(this@ForgotPassword, validation.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun dataValidation(): Validation {
        return if (emailET.text.toString().trim().isEmpty()) {
            Validation(resources.getString(R.string.emailOrPhoneIsEmpty), false)
        } else if (!Utils.isValidEmail(emailET.text.toString().trim())) {
            val phone: String = if (!emailET.text.toString().startsWith("+")) {
                "+" + emailET.text.toString()
            } else {
                emailET.text.toString()
            }
            if (!Utils.isValidPhoneNumber(phone)) {
                Validation(resources.getString(R.string.invalidEmailOrPhone), false)
            } else {
                Validation("Success.", true)
            }
        } else Validation("Success.", true)
    }

}