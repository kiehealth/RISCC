package com.cronelab.riscc.ui.auth.responses

import com.cronelab.riscc.support.data.database.table.DBUser

class UserResponse {
    var user = DBUser()
    var error: Throwable? = Throwable()
    var message = ""
}