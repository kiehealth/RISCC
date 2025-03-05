package com.cronelab.riscc.support.constants

object Constants {
    const val DEFAULT_LANGUAGE_CODE = "en"
    const val LANGUAGE_CODE = "language"
    const val DEVICE_PLATFORM = "ANDROID"
    const val BASE_64_ENCODE_PREFIX = "data:image/jpeg;base64,"

    object RequestCodes {
        const val PERMISSION_REQUEST_CODE = 1000
        const val CAPTURE_IMAGE_REQUEST_CODE = 1002
        const val PICK_IMAGE_FROM_GALLERY_REQUEST_CODE = 1003
        const val WRITE_STORAGE_ACCESS = 1004
        const val CALL_PHONE = 1005
    }
}