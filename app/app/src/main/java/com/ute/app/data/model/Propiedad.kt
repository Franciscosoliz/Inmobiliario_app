package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Propiedad(
    val id: Int,
    val titulo: String,
    val descripcion: String? = null,
    val tipo_inmueble: String,
    val estado_negocio: String,
    val precio: String,
    val direccion: String,
    val habitaciones: Int,
    val banos: Int,
    val area_metros: String,
    val agente: Int,
    val zona: Int,
    val imagen: String? = null,

    val agente_nombre: String? = null,
    val zona_nombre: String? = null
) : Parcelable