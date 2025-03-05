package com.cronelab.riscc.support.exceptions

import java.io.IOException

/**
 * Created by Rajesh Shrestha on 2020-10-19.
 */
class ApiException(message: String) : IOException(message)

class NoInternetException(message: String) : IOException(message)