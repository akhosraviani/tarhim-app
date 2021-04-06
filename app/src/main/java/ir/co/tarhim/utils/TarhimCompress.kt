package ir.co.tarhim.utils

import android.graphics.Bitmap

class TarhimCompress {
    public fun compressImage(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width: Int = bitmap.getWidth()
        var height: Int = bitmap.getHeight()

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio >1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)

    }
}