package com.cronelab.riscc.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.R
import com.cronelab.riscc.support.manager.UserManager
import com.cronelab.riscc.support.singleton.User
import com.cronelab.riscc.ui.auth.login.Login
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.cronelab.riscc.ui.dashboard.Dashboard
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


@SuppressLint("CustomSplashScreen")
class Splashscreen : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val userViewModelFactory: UserViewModelFactory by instance()
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        userViewModel = ViewModelProviders.of(this, userViewModelFactory).get(UserViewModel::class.java)
        navigate()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    private fun navigate() {
        userViewModel.getUser()
        userViewModel.user.observe(this, Observer {
            userViewModel.user.removeObserver { }
            if (!it.isNullOrEmpty()) {
                /*UserManager().saveUser(this, it[0])
                startActivity(Intent(this, Dashboard::class.java))
                finish()*/
                autoLogin()
            } else {
                val thread = Thread {
                    try {
                        Thread.sleep(1000)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
                thread.start()

            }
        })
    }


    private fun navigateToLogin() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    private fun autoLogin() {
        val email = UserManager().getUserEmail()
        val password = UserManager().getUserPassword()
        if (email != null && password != null) {
            userViewModel.loginResponse.observe(this, Observer {
                if (!this.isFinishing) {
                    userViewModel.loginResponse.removeObserver {}
                    if (it.error != null) {
                        navigateToLogin()
                    } else {
                        UserManager().saveUser(this, it.user)
                        User.getInstance(this).user = it.user
                        val intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)
                        this.finish()
                    }
                }
            })
            userViewModel.login(email, password)
        }
    }

}
