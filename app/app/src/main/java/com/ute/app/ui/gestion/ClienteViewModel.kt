package com.ute.app.ui.gestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.app.data.model.ClienteInmobiliario
import com.ute.app.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ClienteUiState {
    object Cargando : ClienteUiState()
    data class Exito(val lista: List<ClienteInmobiliario>) : ClienteUiState()
    data class Error(val mensaje: String) : ClienteUiState()
}

class ClienteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ClienteUiState>(ClienteUiState.Cargando)
    val uiState: StateFlow<ClienteUiState> = _uiState.asStateFlow()

    fun cargarClientes(token: String) {
        viewModelScope.launch {
            _uiState.value = ClienteUiState.Cargando
            try {
                val respuesta = RetrofitClient.apiService.getClientes("Bearer $token")
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    _uiState.value = ClienteUiState.Exito(respuesta.body()!!.results)
                } else {
                    _uiState.value = ClienteUiState.Error("Error: ${respuesta.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = ClienteUiState.Error("Error de red")
            }
        }
    }

    fun crearCliente(token: String, cliente: ClienteInmobiliario, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.crearCliente("Bearer $token", cliente)
                onResult(respuesta.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun actualizarCliente(token: String, cliente: ClienteInmobiliario, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.actualizarCliente("Bearer $token", cliente.id, cliente)
                onResult(respuesta.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun eliminarCliente(token: String, idCliente: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.eliminarCliente("Bearer $token", idCliente)
                onResult(respuesta.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}