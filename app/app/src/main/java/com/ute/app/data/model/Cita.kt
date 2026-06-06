package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Cita(
    val id: Int,
    val propiedad: Int,       // ID numérico original
    val cliente: Int,         // ID numérico original
    val agente: Int,
    val fecha_hora: String,
    val comentarios: String? = null,
    val estado: String,

    // 👈 CAMPOS CLAVE: Mapean las llaves string con los nombres reales que envía el Serializer
    val propiedad_titulo: String? = null,
    val cliente_nombre: String? = null
) : Parcelable