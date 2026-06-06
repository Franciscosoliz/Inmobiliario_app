package com.ute.app.ui.gestion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ute.app.data.model.*

@Composable
fun CrearClienteScreen(viewModel: ClienteViewModel, token: String, onBackClick: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var iden by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var presupuesto by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Nuevo Cliente", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = iden, onValueChange = { iden = it }, label = { Text("Identificación") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = presupuesto, onValueChange = { presupuesto = it }, label = { Text("Presupuesto") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                val nuevo = ClienteInmobiliario(0, nombre, iden, email, telefono, presupuesto)
                viewModel.crearCliente(token, nuevo) { exito ->
                    if (exito) onBackClick()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cliente")
        }
    }
}