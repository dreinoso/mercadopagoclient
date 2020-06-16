package com.chainreaction.mercadopagoclient.util

import org.json.JSONObject
import retrofit.RetrofitError
import retrofit.mime.TypedInput
import java.io.IOException
import java.io.InputStream

object RetrofitUtil {
    private fun getErrorBody(error: TypedInput): String? {
        try {
            val stream: InputStream = error.`in`()
            val bytes = ByteArray(error.length() as Int)
            stream.read(bytes)
            return String(bytes)
        } catch (ignored: IOException) {
        }
        return null
    }

    fun parseErrorBody(e: RetrofitError): JSONObject? {
        try {
            if (e.getResponse() != null) {
                return JSONObject(getErrorBody(e.getResponse().getBody()))
            }
        } catch (ignored: Exception) {
        }
        return null
    }
}