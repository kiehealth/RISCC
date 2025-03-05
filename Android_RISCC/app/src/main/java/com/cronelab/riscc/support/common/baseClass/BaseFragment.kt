package com.cronelab.riscc.support.common.baseClass

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModel
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

open class BaseFragment : Fragment(), KodeinAware {

    private val TAG = "BaseFragment"
    override val kodein by kodein()

    private val userViewModelFactory: UserViewModelFactory by instance()
    private lateinit var userViewModel: UserViewModel
    var user: DBUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProviders.of(requireActivity(), userViewModelFactory).get(UserViewModel::class.java)
        getUser()
    }

    private fun getUser() {
        userViewModel.getUser()
        userViewModel.user.observe(requireActivity(), Observer {
            if (it.isNotEmpty()) {
                userViewModel.user.removeObserver { }
                user = it[0]
                Log.w(TAG, "user's firstName: ${user!!.firstName}")
            }
        })
    }
}