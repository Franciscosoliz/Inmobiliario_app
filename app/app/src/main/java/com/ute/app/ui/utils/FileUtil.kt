package com.ute.app.ui.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

object FileUtil {
    fun prepararMultipartImagen(context: Context, uri: Uri, paramName: String): MultipartBody.Part? {
        val contentResolver = context.contentResolver
        // Creamos un archivo temporal en la caché del celular para copiar los bytes
        val file = File(context.cacheDir, "temp_propiedad_image.jpg")

        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            // "paramName" debe llamarse exactamente igual que tu campo en Django ('imagen')
            MultipartBody.Part.createFormData(paramName, file.name, requestFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}