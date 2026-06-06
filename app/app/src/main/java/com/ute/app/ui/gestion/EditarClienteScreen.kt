package com.ute.app.ui.gestion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ute.app.data.model.ClienteInmobiliario

@Composable
fun EditarClienteScreen(cliente: ClienteInmobiliario, viewModel: ClienteViewModel, token: String, onBackClick: () -> Unit) {
    var nombre by remember { mutableStateOf(cliente.nombre_completo) }
    var email by remember { mutableStateOf(cliente.email) }
    var telefono by remember { mutableStateOf(cliente.telefono) }
    var presupuesto by remember { mutableStateOf(cliente.presupuesto_maximo ?: "") }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Editar Cliente", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = presupuesto, onValueChange = { presupuesto = it }, label = { Text("Presupuesto") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                val actualizado = cliente.copy(
                    nombre_completo = nombre,
                    email = email,
                    telefono = telefono,
                    presupuesto_maximo = presupuesto
                )
                viewModel.actualizarCliente(token, actualizado) { exito ->
                    if (exito) onBackClick()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar Cambios")
        }
    }
}