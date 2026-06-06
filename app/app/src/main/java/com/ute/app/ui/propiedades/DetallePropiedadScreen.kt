package com.ute.app.ui.propiedades

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // 🌟 IMPORTANTE PARA EL CONTEXTO
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ute.app.data.model.Propiedad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePropiedadScreen(
    propiedad: Propiedad,
    onBackClick: () -> Unit
) {
    // 🌟 CAPTURAMOS EL CONTEXTO ACTIVO DE LA APP AQUÍ
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Inmueble", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Imagen Principal de la Casa
            AsyncImage(
                model = propiedad.imagen ?: "https://via.placeholder.com/600x300.png?text=Sin+Foto",
                contentDescription = propiedad.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.Crop
            )

            // Contenido con espaciado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 2. Encabezado: Título y Estado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = propiedad.titulo,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    SuggestionChip(
                        onClick = { },
                        label = { Text(propiedad.estado_negocio) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }

                // 3. Precio Destacado
                Text(
                    text = "$${propiedad.precio}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                // 4. Ubicación Geográfica
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = propiedad.direccion,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // 5. Descripción Completa
                Text(
                    text = "Descripción del Inmueble",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = propiedad.descripcion ?: "No se ha proporcionado una descripción detallada para esta propiedad.",
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // 6. Sección del Agente Inmobiliario Asignado
                Text(
                    text = "Asesor Encargado",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Lic. Nicolás Paredes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "Agente Especializado", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        
                        // 🌟 BOTONES CONECTADOS CON LAS ACCIONES DE ANDROID
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledIconButton(
                                onClick = { hacerLlamada(context, "+593999999999") } // 📞 Abre el marcador de llamadas
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = "Llamar")
                            }
                            FilledIconButton(
                                onClick = { 
                                    enviarCorreo(
                                        context = context, 
                                        correo = "nicolas.paredes@ute.edu.ec", 
                                        asunto = "Interés en la propiedad: ${propiedad.titulo}"
                                    ) 
                                } // ✉️ Abre la app de correos con datos precargados
                            ) {
                                Icon(Icons.Default.Email, contentDescription = "Correo")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Funciones utilitarias (Permanecen intactas al final del archivo)
fun hacerLlamada(context: Context, telefono: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$telefono")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir la app de llamadas", Toast.LENGTH_SHORT).show()
    }
}

fun enviarCorreo(context: Context, correo: String, asunto: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(correo))
            putExtra(Intent.EXTRA_SUBJECT, asunto)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir la app de correo", Toast.LENGTH_SHORT).show()
    }
}