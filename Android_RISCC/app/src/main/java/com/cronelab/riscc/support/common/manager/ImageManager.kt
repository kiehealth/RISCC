package com.cronelab.riscc.support.common.manager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import com.cronelab.riscc.support.constants.Constants
import java.io.ByteArrayOutputStream


class ImageManager {
    private val TAG = "ImageManager"

   /* private fun saveImage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/app_name/saved_images")
        myDir.mkdirs()
        val fname = "Image-" + System.currentTimeMillis() + ".jpg"
        val file = File(myDir, fname)
        Log.e("ImageManager", "filepath: " + file.absolutePath)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

    fun encodeTobBase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        return Constants.BASE_64_ENCODE_PREFIX + Base64.encodeToString(b, Base64.NO_WRAP)
    }

    fun decodeBase64(input: String): Bitmap {
        val decodedByte = Base64.decode(input, Base64.NO_WRAP)
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    fun rotateBitmap(b: Bitmap, degrees: Float): Bitmap? {
        var bitmap = b
        val m = Matrix()
        if (degrees != 0f) {
            // clockwise
            m.postRotate(
                degrees, bitmap.width.toFloat() / 2,
                bitmap.height.toFloat() / 2
            )
        }
        try {
            val b2 = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width,
                bitmap.height, m, true
            )
            if (bitmap != b2) {
                bitmap.recycle()
                bitmap = b2
            }
        } catch (ex: OutOfMemoryError) {
            // We have no memory to rotate. Return the original bitmap.
        }
        return bitmap
    }


}
