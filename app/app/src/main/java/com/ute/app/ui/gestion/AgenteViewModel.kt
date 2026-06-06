package com.ute.app.ui.gestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.app.data.model.Agente
import com.ute.app.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AgenteUiState {
    object Cargando : AgenteUiState()
    data class Exito(val lista: List<Agente>) : AgenteUiState()
    data class Error(val mensaje: String) : AgenteUiState()
}

class AgenteViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<AgenteUiState>(AgenteUiState.Cargando)
    val uiState: StateFlow<AgenteUiState> = _uiState.asStateFlow()

    fun cargarAgentes(token: String) {
        viewModelScope.launch {
            _uiState.value = AgenteUiState.Cargando
            try {
                val respuesta = RetrofitClient.apiService.getAgentes("Bearer $token")
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    _uiState.value = AgenteUiState.Exito(respuesta.body()!!.results)
                } else {
                    _uiState.value = AgenteUiState.Error("Error: ${respuesta.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = AgenteUiState.Error("Error de conexión")
            }
        }
    }

    fun guardarAgente(token: String, agente: Agente, esEdicion: Boolean, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = if (esEdicion) {
                    RetrofitClient.apiService.actualizarAgente("Bearer $token", agente.id, agente)
                } else {
                    RetrofitClient.apiService.crearAgente("Bearer $token", agente)
                }
                onResultado(respuesta.isSuccessful)
            } catch (e: Exception) {
                onResultado(false)
            }
        }
    }

    fun eliminarAgente(token: String, idAgente: Int, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.eliminarAgente("Bearer $token", idAgente)
                onResultado(respuesta.isSuccessful)
            } catch (e: Exception) {
                onResultado(false)
            }
        }
    }
}