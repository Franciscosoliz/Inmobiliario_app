package com.ute.app.data

import com.ute.app.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class AuthRepository {
    
    private val api = RetrofitClient.apiService

    suspend fun login(username: String, password: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                val code = response.code()
                val message = when (code) {
                    400 -> "Datos inválidos (400)"
                    401 -> "Usuario o contraseña incorrectos (401)"
                    403 -> "No tienes permisos para acceder (403)"
                    404 -> "Servicio de autenticación no encontrado (404)"
                    else -> "Error en el servidor: $code"
                }
                emit(Resource.Error(message, code))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Error de conexión. Verifica tu internet."))
        } catch (e: Exception) {
            emit(Resource.Error("Ocurrió un error inesperado: ${e.localizedMessage}"))
        }
    }
}