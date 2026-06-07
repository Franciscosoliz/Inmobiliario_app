package com.ute.app.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            400 -> throw IOException("Datos inválidos (Error 400)")
            401 -> throw IOException("Sesión expirada o credenciales inválidas")
            403 -> throw IOException("No tienes permisos suficientes")
            404 -> throw IOException("Recurso no encontrado")
            in 500..599 -> throw IOException("Error interno del servidor")
        }
        
        return response
    }
}