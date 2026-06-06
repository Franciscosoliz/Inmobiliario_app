package com.ute.app.ui.propiedades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.app.data.repo.InmobiliariaRepository
import com.ute.app.data.model.Propiedad
import com.ute.app.data.model.Agente
import com.ute.app.data.model.Zona
import okhttp3.MultipartBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PropiedadUiState {
    object Cargando : PropiedadUiState
    data class Exito(val lista: List<Propiedad>) : PropiedadUiState
    data class Error(val mensaje: String) : PropiedadUiState
}

class PropiedadViewModel : ViewModel() {

    private val repository = InmobiliariaRepository()

    private val _uiState = MutableStateFlow<PropiedadUiState>(PropiedadUiState.Cargando)
    val uiState: StateFlow<PropiedadUiState> = _uiState.asStateFlow()

    private val _agentes = MutableStateFlow<List<Agente>>(emptyList())
    val agentes: StateFlow<List<Agente>> = _agentes.asStateFlow()

    private val _zonas = MutableStateFlow<List<Zona>>(emptyList())
    val zonas: StateFlow<List<Zona>> = _zonas.asStateFlow()

    fun cargarPropiedades(token: String) {
        viewModelScope.launch {
            _uiState.value = PropiedadUiState.Cargando
            repository.obtenerPropiedades(token)
                .onSuccess { propiedades ->
                    if (propiedades.isEmpty()) _uiState.value = PropiedadUiState.Error("No hay propiedades registradas.")
                    else _uiState.value = PropiedadUiState.Exito(propiedades)
                }
                .onFailure { error -> _uiState.value = PropiedadUiState.Error(error.localizedMessage ?: "Error de red") }
        }
    }

    fun cargarCatalogosFormulario(token: String) {
        viewModelScope.launch {
            repository.obtenerAgentes(token).onSuccess { _agentes.value = it }
            repository.obtenerZonas(token).onSuccess { _zonas.value = it }
        }
    }

    fun guardarPropiedad(token: String, propiedad: Propiedad, imagenPart: MultipartBody.Part?, esEdicion: Boolean, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.guardarPropiedadConImagen(token, propiedad, imagenPart, esEdicion)
                .onSuccess { onResultado(true) }
                .onFailure { onResultado(false) }
        }
    }

    fun borrarPropiedad(token: String, id: Int, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.eliminarPropiedad(token, id)
                .onSuccess { onResultado(true) }
                .onFailure { onResultado(false) }
        }
    }
}