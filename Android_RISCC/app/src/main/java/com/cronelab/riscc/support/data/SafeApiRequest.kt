package com.cronelab.riscc.support.data

import android.util.Log
import com.cronelab.riscc.support.exceptions.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

/**
 * Created by Rajesh Shrestha on 2020-10-19.
 */


abstract class SafeApiRequest {
      suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuffer()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (ex: JSONException) {
                    Log.e("SafeApiRequest", "ex: ${ex.message}")

                } catch (ex: IOException) {
                    Log.e("SafeApiRequest", "ex: ${ex.message}")
                }
                message.append("\n")
            }
            // message.append("Error code: ${response.code()}")
            throw  ApiException(message.toString())
        }
    }
}