package com.ute.app.ui.gestion

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ute.app.data.model.Cita
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarCitaScreen(
    citaAEditar: Cita,
    viewModel: CitaViewModel,
    token: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(token) {
        viewModel.cargarCatalogosFormulario(token)
    }

    val listaPropiedades by viewModel.propiedades.collectAsState()
    val listaClientes by viewModel.clientes.collectAsState()
    val listaAgentes by viewModel.agentes.collectAsState()

    val citaInicial = remember { citaAEditar }

    var fechaHoraIso by remember { mutableStateOf(citaInicial.fecha_hora) }
    var estadoSeleccionado by remember { mutableStateOf(citaInicial.estado) }
    var comentarios by remember { mutableStateOf(citaInicial.comentarios ?: "") }
    var propiedadSeleccionadaId by remember { mutableStateOf<Int?>(citaInicial.propiedad) }
    var clienteSeleccionadoId by remember { mutableStateOf<Int?>(citaInicial.cliente) }
    var agenteSeleccionadoId by remember { mutableStateOf<Int?>(citaInicial.agente) }

    var cargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    var menuEstadoExpandido by remember { mutableStateOf(false) }
    var menuPropiedadExpandido by remember { mutableStateOf(false) }
    var menuClienteExpandido by remember { mutableStateOf(false) }
    var menuAgenteExpandido by remember { mutableStateOf(false) }

    var mostrarDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (mostrarDatePicker) {
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val fechaSeleccionadaLong = datePickerState.selectedDateMillis
                    if (fechaSeleccionadaLong != null) {
                        val calendarioInfo = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calendarioInfo.timeInMillis = fechaSeleccionadaLong

                        TimePickerDialog(
                            context,
                            { _, hora, minuto ->
                                calendarioInfo.set(Calendar.HOUR_OF_DAY, hora)
                                calendarioInfo.set(Calendar.MINUTE, minuto)
                                calendarioInfo.set(Calendar.SECOND, 0)

                                val formatoIso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                                formatoIso.timeZone = TimeZone.getTimeZone("UTC")
                                fechaHoraIso = formatoIso.format(calendarioInfo.time)
                                mostrarDatePicker = false
                            },
                            12, 0, true
                        ).show()
                    } else {
                        mostrarDatePicker = false
                    }
                }) { Text("Siguiente") }
            },
            dismissButton = { TextButton(onClick = { mostrarDatePicker = false }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar Cita") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (mensajeError.isNotEmpty()) {
                Text(text = mensajeError, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = fechaHoraIso,
                onValueChange = {},
                label = { Text("Fecha y Hora de la Cita") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { mostrarDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                }
            )

            val opcionesEstado = listOf("Programada", "Realizada", "Cancelada")
            ExposedDropdownMenuBox(
                expanded = menuEstadoExpandido,
                onExpandedChange = { menuEstadoExpandido = !menuEstadoExpandido }
            ) {
                OutlinedTextField(
                    value = estadoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado de la Cita") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuEstadoExpandido) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = menuEstadoExpandido, onDismissRequest = { menuEstadoExpandido = false }) {
                    opcionesEstado.forEach { item ->
                        DropdownMenuItem(text = { Text(item) }, onClick = { estadoSeleccionado = item; menuEstadoExpandido = false })
                    }
                }
            }

            val propiedadVisual = listaPropiedades.find { it.id == propiedadSeleccionadaId }?.let { "${it.titulo} (ID: ${it.id})" } ?: "Seleccionar Propiedad"
            ExposedDropdownMenuBox(
                expanded = menuPropiedadExpandido,
                onExpandedChange = { menuPropiedadExpandido = !menuPropiedadExpandido }
            ) {
                OutlinedTextField(
                    value = propiedadVisual,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Inmueble / Propiedad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuPropiedadExpandido) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = menuPropiedadExpandido, onDismissRequest = { menuPropiedadExpandido = false }) {
                    listaPropiedades.forEach { prop ->
                        DropdownMenuItem(text = { Text("${prop.titulo} - \$${prop.precio}") }, onClick = { propiedadSeleccionadaId = prop.id; menuPropiedadExpandido = false })
                    }
                }
            }

            val clienteVisual = listaClientes.find { it.id == clienteSeleccionadoId }?.let { "${it.nombre_completo} (ID: ${it.id})" } ?: "Seleccionar Cliente"
            ExposedDropdownMenuBox(
                expanded = menuClienteExpandido,
                onExpandedChange = { menuClienteExpandido = !menuClienteExpandido }
            ) {
                OutlinedTextField(
                    value = clienteVisual,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cliente Interesado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuClienteExpandido) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = menuClienteExpandido, onDismissRequest = { menuClienteExpandido = false }) {
                    listaClientes.forEach { cli ->
                        DropdownMenuItem(text = { Text(cli.nombre_completo) }, onClick = { clienteSeleccionadoId = cli.id; menuClienteExpandido = false })
                    }
                }
            }

            val agenteVisual = listaAgentes.find { it.id == agenteSeleccionadoId }?.let { "${it.licencia_profesional} (ID: ${it.id})" } ?: "Seleccionar Agente"
            ExposedDropdownMenuBox(
                expanded = menuAgenteExpandido,
                onExpandedChange = { menuAgenteExpandido = !menuAgenteExpandido }
            ) {
                OutlinedTextField(
                    value = agenteVisual,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Agente Inmobiliario Asignado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAgenteExpandido) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = menuAgenteExpandido, onDismissRequest = { menuAgenteExpandido = false }) {
                    listaAgentes.forEach { ag ->
                        DropdownMenuItem(text = { Text(ag.licencia_profesional) }, onClick = { agenteSeleccionadoId = ag.id; menuAgenteExpandido = false })
                    }
                }
            }

            OutlinedTextField(
                value = comentarios,
                onValueChange = { comentarios = it },
                label = { Text("Comentarios u Observaciones") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val pId = propiedadSeleccionadaId
                    val cId = clienteSeleccionadoId
                    val aId = agenteSeleccionadoId

                    if (fechaHoraIso.isEmpty()) {
                        mensajeError = "Por favor, selecciona una fecha y hora válida."
                        return@Button
                    }
                    if (pId == null || cId == null || aId == null) {
                        mensajeError = "Debes asociar una Propiedad, Cliente y Agente obligatoriamente."
                        return@Button
                    }

                    cargando = true
                    val citaActualizada = Cita(id = citaInicial.id, propiedad = pId, cliente = cId, agente = aId, fecha_hora = fechaHoraIso, comentarios = comentarios, estado = estadoSeleccionado)

                    viewModel.guardarCita(token, citaActualizada, esEdicion = true) { exitoso ->
                        cargando = false
                        if (exitoso) onBackClick() else mensajeError = "Error en el servidor al procesar la solicitud."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                if (cargando) CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                else Text("Actualizar Cambios")
            }

            TextButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth(), enabled = !cargando) { Text("Cancelar") }
        }
    }
}