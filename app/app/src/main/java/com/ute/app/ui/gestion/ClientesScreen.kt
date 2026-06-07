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
import com.ute.app.data.model.ClienteInmobiliario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    viewModel: ClienteViewModel,
    token: String,
    isStaff: Boolean,
    onEditarClick: (ClienteInmobiliario) -> Unit,
    onAddClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var busqueda by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.cargarClientes(token) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Cartera de Clientes") }) },
        floatingActionButton = {
            if (isStaff) {
                FloatingActionButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Cliente")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Filtrar por nombre...") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is ClienteUiState.Cargando -> {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                    is ClienteUiState.Exito -> {
                        val listaFiltrada = state.lista.filter {
                            it.nombre_completo.contains(busqueda, ignoreCase = true)
                        }

                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            items(listaFiltrada) { cliente ->
                                ListItem(
                                    headlineContent = { Text(cliente.nombre_completo) },
                                    supportingContent = { Text("Tel: ${cliente.telefono} • ${cliente.email}") },
                                    trailingContent = {
                                        if (isStaff) {
                                            Row {
                                                IconButton(onClick = { onEditarClick(cliente) }) {
                                                    Icon(Icons.Default.Edit, "Editar")
                                                }
                                                IconButton(onClick = {
                                                    viewModel.eliminarCliente(token, cliente.id) { exito ->
                                                        if (exito) viewModel.cargarClientes(token)
                                                    }
                                                }) {
                                                    Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
                                                }
                                            }
                                        }
                                    },
                                    leadingContent = {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Text(cliente.nombre_completo.first().toString(), modifier = Modifier.padding(8.dp))
                                        }
                                    }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                    is ClienteUiState.Error -> {
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