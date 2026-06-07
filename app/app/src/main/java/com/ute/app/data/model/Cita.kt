package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Cita(
    val id: Int,
    val propiedad: Int,
    val cliente: Int,
    val agente: Int,
    val fecha_hora: String,
    val comentarios: String? = null,
    val estado: String,

    val propiedad_titulo: String? = null,
    val cliente_nombre: String? = null
) : Parcelable