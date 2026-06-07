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
import com.ute.app.data.model.Agente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentesScreen(
    viewModel: AgenteViewModel,
    token: String,
    isStaff: Boolean,
    onAddClick: () -> Unit,
    onEditarClick: (Agente) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var busqueda by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.cargarAgentes(token) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuestros Agentes") }) },
        floatingActionButton = {
            if (isStaff) {
                FloatingActionButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Filtrar por licencia...") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            when (val state = uiState) {
                is AgenteUiState.Cargando -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                is AgenteUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) { Text(state.mensaje, color = MaterialTheme.colorScheme.error) }
                is AgenteUiState.Exito -> {
                    val listaFiltrada = state.lista.filter {
                        it.licencia_profesional.contains(busqueda, ignoreCase = true)
                    }

                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(listaFiltrada) { agente ->
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = agente.licencia_profesional, style = MaterialTheme.typography.titleMedium)
                                        Text(text = "Tel: ${agente.telefono}", style = MaterialTheme.typography.bodyMedium)
                                    }

                                    if (isStaff) {
                                        IconButton(onClick = { onEditarClick(agente) }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                                        }
                                        IconButton(onClick = {
                                            viewModel.eliminarAgente(token, agente.id) { exito ->
                                                if (exito) viewModel.cargarAgentes(token)
                                            }
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}