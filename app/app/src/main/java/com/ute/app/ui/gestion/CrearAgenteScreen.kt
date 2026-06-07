package com.ute.app.ui.gestion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ute.app.data.model.Agente
import com.ute.app.data.model.UserDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearAgenteScreen(viewModel: AgenteViewModel, token: String, onBackClick: () -> Unit) {
    var licencia by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Registrar Agente") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = licencia, onValueChange = { licencia = it }, label = { Text("Licencia Profesional") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = especialidad, onValueChange = { especialidad = it }, label = { Text("Especialidad") }, modifier = Modifier.fillMaxWidth())

            Button(onClick = {
                val nuevoAgente = Agente(
                    id = 0,
                    user = 1,
                    user_detail = null,
                    licencia_profesional = licencia,
                    telefono = telefono,
                    especialidad = especialidad,
                    activo = true
                )
                viewModel.guardarAgente(token, nuevoAgente, false) { if (it) onBackClick() }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Guardar Agente")
            }
        }
    }
}