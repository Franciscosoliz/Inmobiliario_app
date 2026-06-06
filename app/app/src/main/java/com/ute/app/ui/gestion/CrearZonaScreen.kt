package com.ute.app.ui.gestion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// 1. Asegúrate de importar tus modelos y el ViewModel
import com.ute.app.data.model.Zona

@Composable
fun CrearZonaScreen(viewModel: ZonaViewModel, token: String, onBackClick: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("Quito") }
    var codPostal by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Zona") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = codPostal, onValueChange = { codPostal = it }, label = { Text("Código Postal") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                // 2. Instanciamos con todos los campos del modelo
                val nuevaZona = Zona(id = 0, nombre = nombre, ciudad = ciudad, codigo_postal = codPostal)

                // 3. Llamamos al método que definimos en el ViewModel (crearZona)
                viewModel.crearZona(token, nuevaZona) { exito ->
                    if (exito) onBackClick()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Zona")
        }
    }
}