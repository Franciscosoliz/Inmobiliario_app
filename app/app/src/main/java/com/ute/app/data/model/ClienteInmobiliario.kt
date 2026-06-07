package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ClienteInmobiliario(
    val id: Int,
    val nombre_completo: String,
    val identificacion: String,
    val email: String,
    val telefono: String,
    val presupuesto_maximo: String? = null
) : Parcelable