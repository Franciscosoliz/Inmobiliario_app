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
import com.ute.app.data.model.Cita

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(
    viewModel: CitaViewModel,
    token: String,
    isStaff: Boolean, // 👈 Control de acceso
    onCrearCitaClick: () -> Unit,
    onEditarCitaClick: (Cita) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var citaIdABorrar by remember { mutableStateOf<Int?>(null) }
    var busqueda by remember { mutableStateOf("") } // 👈 Filtro

    LaunchedEffect(token) { viewModel.cargarCitas(token) }

    if (citaIdABorrar != null) {
        AlertDialog(
            onDismissRequest = { citaIdABorrar = null },
            title = { Text("¿Eliminar Cita?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    citaIdABorrar?.let { id ->
                        viewModel.borrarCita(token, id) { exitoso ->
                            if (exitoso) { citaIdABorrar = null; viewModel.cargarCitas(token) }
                        }
                    }
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { citaIdABorrar = null }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gestión de Citas") }) },
        floatingActionButton = {
            if (isStaff) { // 👈 Solo staff crea citas
                FloatingActionButton(onClick = onCrearCitaClick) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva Cita")
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Campo de búsqueda
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por nombre de cliente...") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is CitaUiState.Cargando -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                    is CitaUiState.Exito -> {
                        // 👈 Filtrado por nombre de cliente
                        val lista = state.lista.filter {
                            it.cliente_nombre?.contains(busqueda, ignoreCase = true) == true
                        }

                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            items(lista) { cita ->
                                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) {
                                            Text("Fecha: ${cita.fecha_hora}", style = MaterialTheme.typography.titleMedium)
                                            Text("Cliente: ${cita.cliente_nombre ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                                        }

                                        if (isStaff) { // 👈 Solo staff puede editar/eliminar
                                            Row {
                                                IconButton(onClick = { onEditarCitaClick(cita) }) {
                                                    Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.primary)
                                                }
                                                IconButton(onClick = { citaIdABorrar = cita.id }) {
                                                    Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}