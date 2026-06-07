package com.ute.app.ui.gestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.app.data.model.*
import com.ute.app.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CitaUiState {
    object Cargando : CitaUiState()
    data class Exito(val lista: List<Cita>) : CitaUiState()
    data class Error(val mensaje: String) : CitaUiState()
}

class CitaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CitaUiState>(CitaUiState.Cargando)
    val uiState: StateFlow<CitaUiState> = _uiState.asStateFlow()

    private val _propiedades = MutableStateFlow<List<Propiedad>>(emptyList())
    val propiedades: StateFlow<List<Propiedad>> = _propiedades.asStateFlow()

    private val _clientes = MutableStateFlow<List<ClienteInmobiliario>>(emptyList())
    val clientes: StateFlow<List<ClienteInmobiliario>> = _clientes.asStateFlow()

    private val _agentes = MutableStateFlow<List<Agente>>(emptyList())
    val agentes: StateFlow<List<Agente>> = _agentes.asStateFlow()

    fun cargarCitas(token: String) {
        viewModelScope.launch {
            _uiState.value = CitaUiState.Cargando
            try {
                val respuesta = RetrofitClient.apiService.getCitas("Bearer $token")
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    _uiState.value = CitaUiState.Exito(respuesta.body()!!.results)
                } else {
                    _uiState.value = CitaUiState.Error("Error al cargar: ${respuesta.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = CitaUiState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun cargarCatalogosFormulario(token: String) {
        viewModelScope.launch {
            val authHeader = "Bearer $token"
            try {
                val resProp = RetrofitClient.apiService.getPropiedades(authHeader)
                if (resProp.isSuccessful && resProp.body() != null) {
                    _propiedades.value = resProp.body()!!.results
                }

                val resCli = RetrofitClient.apiService.getClientes(authHeader)
                if (resCli.isSuccessful && resCli.body() != null) {
                    _clientes.value = resCli.body()!!.results
                }

                val resAg = RetrofitClient.apiService.getAgentes(authHeader)
                if (resAg.isSuccessful && resAg.body() != null) {
                    _agentes.value = resAg.body()!!.results
                }
            } catch (e: Exception) {
            }
        }
    }

    fun guardarCita(token: String, cita: Cita, esEdicion: Boolean, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = if (esEdicion) {
                    RetrofitClient.apiService.actualizarCita("Bearer $token", cita.id, cita)
                } else {
                    RetrofitClient.apiService.crearCita("Bearer $token", cita)
                }

                if (respuesta.isSuccessful) {
                    cargarCitas(token)
                    onResultado(true)
                } else {
                    onResultado(false)
                }
            } catch (e: Exception) {
                onResultado(false)
            }
        }
    }

    fun borrarCita(token: String, id: Int, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.eliminarCita("Bearer $token", id)
                if (respuesta.isSuccessful) {
                    cargarCitas(token)
                    onResultado(true)
                } else {
                    onResultado(false)
                }
            } catch (e: Exception) {
                onResultado(false)
            }
        }
    }
}