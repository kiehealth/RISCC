package com.cronelab.riscc.ui.auth.responses

import com.cronelab.riscc.ui.auth.pojo.Verification

class VerificationCodeResponse {
    var verification = Verification()
    var error: Throwable? = Throwable()
    var message = ""
}
