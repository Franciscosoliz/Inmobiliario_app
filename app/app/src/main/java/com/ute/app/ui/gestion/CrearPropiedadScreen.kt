package com.ute.app.ui.propiedades

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ute.app.data.model.Propiedad
import com.ute.app.ui.utils.FileUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPropiedadScreen(
    viewModel: PropiedadViewModel,
    token: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(token) { viewModel.cargarCatalogosFormulario(token) }

    val listaAgentes by viewModel.agentes.collectAsState()
    val listaZonas by viewModel.zonas.collectAsState()

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tipoInmueble by remember { mutableStateOf("Casa") }
    var estadoNegocio by remember { mutableStateOf("Disponible") }
    var precio by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var habitaciones by remember { mutableStateOf("") }
    var banos by remember { mutableStateOf("") }
    var areaMetros by remember { mutableStateOf("") }
    var agenteSeleccionadoId by remember { mutableStateOf<Int?>(null) }
    var zonaSeleccionadoId by remember { mutableStateOf<Int?>(null) }

    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    var cargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    var menuTipoExpandido by remember { mutableStateOf(false) }
    var menuEstadoExpandido by remember { mutableStateOf(false) }
    var menuAgenteExpandido by remember { mutableStateOf(false) }
    var menuZonaExpandido by remember { mutableStateOf(false) }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imagenUri = uri }

    Scaffold(topBar = { TopAppBar(title = { Text("Registrar Inmueble") }) }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (mensajeError.isNotEmpty()) Text(text = mensajeError, color = MaterialTheme.colorScheme.error)

            OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título de la Propiedad *") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), minLines = 2)

            ExposedDropdownMenuBox(expanded = menuTipoExpandido, onExpandedChange = { menuTipoExpandido = !menuTipoExpandido }) {
                OutlinedTextField(value = tipoInmueble, onValueChange = {}, readOnly = true, label = { Text("Tipo de Inmueble") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuTipoExpandido) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = menuTipoExpandido, onDismissRequest = { menuTipoExpandido = false }) {
                    listOf("Casa", "Departamento", "Oficina", "Terreno").forEach { item -> DropdownMenuItem(text = { Text(item) }, onClick = { tipoInmueble = item; menuTipoExpandido = false }) }
                }
            }

            ExposedDropdownMenuBox(expanded = menuEstadoExpandido, onExpandedChange = { menuEstadoExpandido = !menuEstadoExpandido }) {
                OutlinedTextField(value = estadoNegocio, onValueChange = {}, readOnly = true, label = { Text("Estado del Negocio") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuEstadoExpandido) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = menuEstadoExpandido, onDismissRequest = { menuEstadoExpandido = false }) {
                    listOf("Disponible", "Arrendado", "Vendido").forEach { item -> DropdownMenuItem(text = { Text(item) }, onClick = { estadoNegocio = item; menuEstadoExpandido = false }) }
                }
            }

            OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio ($) *") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección Exacta") }, modifier = Modifier.fillMaxWidth())

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = habitaciones, onValueChange = { habitaciones = it }, label = { Text("Habitaciones") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                OutlinedTextField(value = banos, onValueChange = { banos = it }, label = { Text("Baños") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
            }

            OutlinedTextField(value = areaMetros, onValueChange = { areaMetros = it }, label = { Text("Área (m²)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

            val agenteVisual = listaAgentes.find { it.id == agenteSeleccionadoId }?.licencia_profesional ?: "Seleccionar Agente *"
            ExposedDropdownMenuBox(expanded = menuAgenteExpandido, onExpandedChange = { menuAgenteExpandido = !menuAgenteExpandido }) {
                OutlinedTextField(value = agenteVisual, onValueChange = {}, readOnly = true, label = { Text("Agente") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAgenteExpandido) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = menuAgenteExpandido, onDismissRequest = { menuAgenteExpandido = false }) {
                    listaAgentes.forEach { ag -> DropdownMenuItem(text = { Text(ag.licencia_profesional) }, onClick = { agenteSeleccionadoId = ag.id; menuAgenteExpandido = false }) }
                }
            }

            val zonaVisual = listaZonas.find { it.id == zonaSeleccionadoId }?.nombre ?: "Seleccionar Zona *"
            ExposedDropdownMenuBox(expanded = menuZonaExpandido, onExpandedChange = { menuZonaExpandido = !menuZonaExpandido }) {
                OutlinedTextField(value = zonaVisual, onValueChange = {}, readOnly = true, label = { Text("Zona") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuZonaExpandido) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = menuZonaExpandido, onDismissRequest = { menuZonaExpandido = false }) {
                    listaZonas.forEach { z -> DropdownMenuItem(text = { Text(z.nombre) }, onClick = { zonaSeleccionadoId = z.id; menuZonaExpandido = false }) }
                }
            }

            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Fotografía del Inmueble", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { galeriaLauncher.launch("image/*") }) {
                        Text(if (imagenUri != null) "🔄 Cambiar Imagen" else "🖼️ Seleccionar desde Galería")
                    }
                    if (imagenUri != null) {
                        Text(text = "Imagen lista para cargarse", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val aId = agenteSeleccionadoId
                    val zId = zonaSeleccionadoId
                    if (titulo.isEmpty() || precio.isEmpty() || aId == null || zId == null) {
                        mensajeError = "Completa los campos marcados con asterisco (*)"
                        return@Button
                    }

                    cargando = true
                    val partImagen = imagenUri?.let { uri -> FileUtil.prepararMultipartImagen(context, uri, "imagen") }
                    val payload = Propiedad(
                        id = 0, titulo = titulo, descripcion = descripcion.ifEmpty { null },
                        tipo_inmueble = tipoInmueble, estado_negocio = estadoNegocio, precio = precio,
                        direccion = direccion, habitaciones = habitaciones.toIntOrNull() ?: 0,
                        banos = banos.toIntOrNull() ?: 0, area_metros = areaMetros.ifEmpty { "0" },
                        agente = aId, zona = zId
                    )

                    viewModel.guardarPropiedad(token, payload, partImagen, esEdicion = false) { exitoso ->
                        cargando = false
                        if (exitoso) onBackClick() else mensajeError = "Error al comunicarse con la API de Django."
                    }
                },
                modifier = Modifier.fillMaxWidth(), enabled = !cargando
            ) {
                if (cargando) CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                else Text("Registrar Propiedad")
            }

            TextButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth(), enabled = !cargando) { Text("Cancelar") }
        }
    }
}