package com.ute.app.ui.gestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.app.data.model.Zona
import com.ute.app.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Declaramos los estados específicos para la pantalla de zonas inmobiliarias
sealed class ZonaUiState {
    object Cargando : ZonaUiState()
    data class Exito(val lista: List<Zona>) : ZonaUiState()
    data class Error(val mensaje: String) : ZonaUiState()
}

class ZonaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ZonaUiState>(ZonaUiState.Cargando)
    val uiState: StateFlow<ZonaUiState> = _uiState.asStateFlow()

    fun cargarZonas(token: String) {
        viewModelScope.launch {
            _uiState.value = ZonaUiState.Cargando
            try {
                val respuesta = RetrofitClient.apiService.getZonas("Bearer $token")

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    // 👈 CORREGIDO: Extraemos la lista del campo 'results'
                    val listaZonas = respuesta.body()!!.results
                    _uiState.value = ZonaUiState.Exito(listaZonas)
                } else {
                    _uiState.value = ZonaUiState.Error("Error del servidor: ${respuesta.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = ZonaUiState.Error("Error de red: No se pudo conectar con el servidor")
            }
        }
    }

    fun crearZona(token: String, zona: Zona, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.crearZona("Bearer $token", zona)
                onResult(respuesta.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun actualizarZona(token: String, zona: Zona, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.actualizarZona("Bearer $token", zona.id, zona)
                onResult(respuesta.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun eliminarZona(token: String, idZona: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.eliminarZona("Bearer $token", idZona)
                onResult(respuesta.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}