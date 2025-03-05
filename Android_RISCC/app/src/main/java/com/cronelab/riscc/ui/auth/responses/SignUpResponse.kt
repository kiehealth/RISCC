package com.cronelab.riscc.ui.auth.responses

import com.cronelab.riscc.support.data.database.table.DBUser

class SignUpResponse {
    var user = DBUser()
    var error: Throwable? = null
    var message = ""
}