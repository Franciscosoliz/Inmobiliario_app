package com.ute.app.ui.gestion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ute.app.data.model.Agente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarAgenteScreen(
    agente: Agente,
    viewModel: AgenteViewModel,
    token: String,
    onBackClick: () -> Unit
) {
    var licencia by remember { mutableStateOf(agente.licencia_profesional) }
    var telefono by remember { mutableStateOf(agente.telefono) }
    var especialidad by remember { mutableStateOf(agente.especialidad) }

    Scaffold(topBar = { TopAppBar(title = { Text("Editar Agente") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = licencia, onValueChange = { licencia = it }, label = { Text("Licencia Profesional") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = especialidad, onValueChange = { especialidad = it }, label = { Text("Especialidad") }, modifier = Modifier.fillMaxWidth())

            Button(onClick = {
                val agenteActualizado = agente.copy(
                    licencia_profesional = licencia,
                    telefono = telefono,
                    especialidad = especialidad
                )
                viewModel.guardarAgente(token, agenteActualizado, true) { if (it) onBackClick() }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Actualizar Agente")
            }
        }
    }
}