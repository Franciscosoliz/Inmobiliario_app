package com.ute.app.ui.propiedades

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ute.app.data.model.Propiedad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropiedadesScreen(
    viewModel: PropiedadViewModel,
    token: String,
    isStaff: Boolean,
    onPropiedadClick: (Propiedad) -> Unit,
    onAddClick: () -> Unit,
    onEditarClick: (Propiedad) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var propiedadIdABorrar by remember { mutableStateOf<Int?>(null) }
    var busqueda by remember { mutableStateOf("") }

    LaunchedEffect(token) { viewModel.cargarPropiedades(token) }

    if (propiedadIdABorrar != null) {
        AlertDialog(
            onDismissRequest = { propiedadIdABorrar = null },
            title = { Text("¿Eliminar Propiedad?") },
            confirmButton = {
                TextButton(onClick = {
                    propiedadIdABorrar?.let { id ->
                        viewModel.borrarPropiedad(token, id) { exitoso ->
                            if (exitoso) { propiedadIdABorrar = null; viewModel.cargarPropiedades(token) }
                        }
                    }
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { propiedadIdABorrar = null }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Catálogo Inmobiliario") }) },
        floatingActionButton = {
            if (isStaff) {
                FloatingActionButton(onClick = onAddClick) { Icon(Icons.Default.Add, "Agregar") }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Filtrar por título...") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            when (val state = uiState) {
                is PropiedadUiState.Cargando -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                is PropiedadUiState.Exito -> {
                    val listaFiltrada = state.lista.filter { it.titulo.contains(busqueda, true) }
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(listaFiltrada) { propiedad ->
                            PropiedadCard(
                                propiedad = propiedad,
                                onClick = { onPropiedadClick(propiedad) },
                                onEdit = { onEditarClick(propiedad) },
                                onDelete = { propiedadIdABorrar = propiedad.id },
                                isStaff = isStaff
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun PropiedadCard(
    propiedad: Propiedad,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    isStaff: Boolean
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column {
            AsyncImage(model = propiedad.imagen ?: "https://via.placeholder.com/400x200.png", contentDescription = null, modifier = Modifier.fillMaxWidth().height(180.dp), contentScale = ContentScale.Crop)
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = propiedad.titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = propiedad.direccion, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (isStaff) {
                        Row {
                            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.primary) }
                            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error) }
                        }
                    }
                }
            }
        }
    }
}