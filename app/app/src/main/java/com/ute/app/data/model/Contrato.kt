package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Contrato(
    val id: Int,
    val propiedad: Int,
    val cliente: Int,
    val tipo: String,
    val monto_total_final: String,
    val fecha_firma: String,
    val vigente: Boolean
) : Parcelable