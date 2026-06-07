package com.ute.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.app.data.LoginRequest
import com.ute.app.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Cargando : LoginUiState
    data class Exito(val token: String, val isStaff: Boolean) : LoginUiState
    data class Error(val mensaje: String) : LoginUiState
}

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun iniciarSesion(usuario: String, contrasenia: String) {
        if (usuario.isBlank() || contrasenia.isBlank()) {
            _uiState.value = LoginUiState.Error("Por favor, llena todos los campos")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Cargando
            try {
                val respuesta = RetrofitClient.apiService.login(LoginRequest(usuario, contrasenia))

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val body = respuesta.body()!!

                    _uiState.value = LoginUiState.Exito(body.access, body.is_staff)
                } else {
                    _uiState.value = LoginUiState.Error("Usuario o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Error: ${e.message ?: "Conexión fallida"}")
            }
        }
    }

    fun resetearEstado() {
        _uiState.value = LoginUiState.Idle
    }

    fun cerrarSesion(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            resetearEstado()
            onLogoutSuccess()
        }
    }
}