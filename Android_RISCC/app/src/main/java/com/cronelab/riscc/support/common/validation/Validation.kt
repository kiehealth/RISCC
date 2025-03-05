package com.cronelab.riscc.support.common.validation


class Validation {
    var message: String
    var status: Boolean

    constructor() {
        message = "NA"
        status = false
    }

    constructor(message: String, status: Boolean) {
        this.message = message
        this.status = status
    }
}
