package com.cronelab.riscc.support.singleton

import android.content.Context
import com.cronelab.riscc.support.data.database.table.DBUser
import com.cronelab.riscc.support.manager.UserManager

/*

class User private constructor() {

    private object UserHolder {
        val INSTANCE = User()
    }

    companion object {
        val instance: User by lazy { UserHolder.INSTANCE }
    }
}*/


class User private constructor(context: Context) {
    var user: DBUser? = null

    init {
        //context.showToast("This is singleton class")
        user = UserManager().getUser(context)!!
    }

    companion object : SingletonHolder<User, Context>(::User)
}

