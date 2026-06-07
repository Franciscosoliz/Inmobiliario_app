package com.ute.app.ui.gestion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ute.app.data.model.Zona

@Composable
fun EditarZonaScreen(zona: Zona, viewModel: ZonaViewModel, token: String, onBackClick: () -> Unit) {
    var nombre by remember { mutableStateOf(zona.nombre) }
    var ciudad by remember { mutableStateOf(zona.ciudad) }
    var codPostal by remember { mutableStateOf(zona.codigo_postal ?: "") }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Editar Zona", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre Zona") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ciudad,
            onValueChange = { ciudad = it },
            label = { Text("Ciudad") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = codPostal,
            onValueChange = { codPostal = it },
            label = { Text("Código Postal") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val zonaEditada = zona.copy(
                    nombre = nombre,
                    ciudad = ciudad,
                    codigo_postal = codPostal
                )

                viewModel.actualizarZona(token, zonaEditada) { exito ->
                    if (exito) onBackClick()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar Zona")
        }
    }
}