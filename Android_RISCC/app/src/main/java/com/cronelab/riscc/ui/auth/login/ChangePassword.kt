package com.cronelab.riscc.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.R
import com.cronelab.riscc.support.common.baseClass.BaseActivity
import com.cronelab.riscc.support.common.showToast
import com.cronelab.riscc.support.common.validation.Validation
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.cronelab.riscc.ui.progressDialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_change_password.*
import org.kodein.di.generic.instance

class ChangePassword : BaseActivity() {

    private lateinit var userViewModel: UserViewModel
    private val userViewModelFactory: UserViewModelFactory by instance()
    private var dbUser: DBUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_change_password)
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
        dbUser = User.getInstance(this).user
    }

    fun navigateBack(view: View) {
        finish()
    }

    private fun validateData(): Validation {
        return when {
            oldPasswordET.text.toString().trim().isEmpty() -> {
                Validation(getString(R.string.oldPasswordEmptyMsg), false)
            }
            newPasswordET.text.toString().trim().isEmpty() -> {
                Validation(getString(R.string.newPasswordEmptyMsg), false)
            }
            confirmNewPasswordET.text.toString().trim().isEmpty() -> {
                Validation(getString(R.string.passwordDoesNotMatch), false)
            }
            !newPasswordET.text.toString().equals(confirmNewPasswordET.text.toString(), ignoreCase = true) -> {
                Validation(getString(R.string.passwordDoesNotMatch), false)
            }
            else -> Validation("Success.", true)
        }

    }

    fun changePassword(view: View) {
        val validation = validateData()
        if (validation.status) {
            val progressDialog = ProgressDialog.loadingProgressDialog(this@ChangePassword, getString(R.string.changingPassword))
            progressDialog.show()
            dbUser?.let {
                userViewModel.changePassword(it, oldPasswordET.text.toString(), newPasswordET.text.toString())
                userViewModel.changePasswordResponse.observe(this, { response ->
                    if (!this.isFinishing) {
                        progressDialog.dismiss()
                        if (response.error == null) {
                            showToast(getString(R.string.passwordChangedPleaseLoginAgain))
                            val intent = Intent(this@ChangePassword, Login::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            showToast(response.message)
                        }
                    }
                })
            }
        } else {
            showToast(validation.message)
        }
    }

}