package com.ute.app.ui.gestion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ute.app.data.model.Zona

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZonasScreen(
    viewModel: ZonaViewModel,
    token: String,
    isStaff: Boolean, // 👈 Control de acceso
    onEditarClick: (Zona) -> Unit,
    onAddClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var busqueda by remember { mutableStateOf("") } // 👈 Filtro

    LaunchedEffect(Unit) { viewModel.cargarZonas(token) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Zonas de Operación") }) },
        floatingActionButton = {
            if (isStaff) { // 👈 Solo staff puede crear
                FloatingActionButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Zona")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Campo de búsqueda
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Filtrar por ciudad o nombre...") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is ZonaUiState.Cargando -> {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                    is ZonaUiState.Exito -> {
                        // 👈 Filtrado de la lista
                        val listaFiltrada = state.lista.filter {
                            it.nombre.contains(busqueda, ignoreCase = true) ||
                                    it.ciudad.contains(busqueda, ignoreCase = true)
                        }

                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            items(listaFiltrada) { zona ->
                                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) {
                                            Text(text = zona.nombre, style = MaterialTheme.typography.titleMedium)
                                            Text(text = "Ciudad: ${zona.ciudad}", style = MaterialTheme.typography.bodyMedium)
                                        }

                                        if (isStaff) { // 👈 Solo staff puede editar/eliminar
                                            IconButton(onClick = { onEditarClick(zona) }) {
                                                Icon(Icons.Default.Edit, "Editar")
                                            }
                                            IconButton(onClick = {
                                                viewModel.eliminarZona(token, zona.id) { exito ->
                                                    if (exito) viewModel.cargarZonas(token)
                                                }
                                            }) {
                                                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is ZonaUiState.Error -> {
                        Text(
                            text = state.mensaje,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center).padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}