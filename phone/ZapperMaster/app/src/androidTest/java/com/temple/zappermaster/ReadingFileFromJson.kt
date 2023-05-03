package com.temple.zappermaster

import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


object ResourceHelper {
    fun loadString(context: Context, name: String): String? {
        val assets = context.assets ?: return null
        try {
            assets.open(name).use { input -> return loadString(input) }
        } catch (e: IOException) {
            return null
        }
    }

    fun loadString(inputStream: InputStream?): String? {
        if (inputStream == null) {
            return null
        }
        try {
            ByteArrayOutputStream().use { result ->
                val buffer = ByteArray(4096)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    result.write(buffer, 0, length)
                }
                return result.toString("UTF-8")
            }
        } catch (e: IOException) {
            return null
        }
    }
}