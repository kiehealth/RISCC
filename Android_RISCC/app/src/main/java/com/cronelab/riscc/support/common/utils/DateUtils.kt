package com.cronelab.riscc.support.common.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Rajesh Shrestha on 2020-11-19.
 */
object DateUtils {
    const val TAG = "DateUtils"

    const val format1 = "yyyy-MM-dd hh:mm:ss aa"
    const val format2 = "dd, MMM"
    const val format3 = "EEEE, dd MMM, yyyy"
    const val format4 = "MMM dd, yyyy"
    const val format5 = "MMM dd, yyyy hh:mm aa"


    fun formatDate(milliseconds: Long?, format: String): String {
        return if (milliseconds != null) {
            val currentFormatter = SimpleDateFormat(format1, Locale.ENGLISH)
            val formattedDate = currentFormatter.format(milliseconds)
            val dateObj = currentFormatter.parse(formattedDate)
            val postFormatter = SimpleDateFormat(format, Locale.ENGLISH)
            if (dateObj != null) {
                postFormatter.format(dateObj)
            } else {
                ""
            }
        } else {
            ""
        }
    }

    fun getEpocTime(date: Date): Long {  //Returns epoch time in millisecond(13 digit)
        val sdf = SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.ENGLISH)
        val formattedDate = sdf.format(date)
        val spDate = formattedDate.split("-".toRegex()).toTypedArray()
        return if (spDate[0].length < 2 && spDate[1].length < 2 && spDate[2].length < 4) 0 else {
            try {
                val df: DateFormat = SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.ENGLISH)
                val d = df.parse(formattedDate)
                d.time
            } catch (e: ParseException) {
                0
            }
        }
    }

}