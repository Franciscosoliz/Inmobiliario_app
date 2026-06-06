package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Contrato(
    val id: Int,
    val propiedad: Int,          // ID de la propiedad (OneToOneField)
    val cliente: Int,            // ID del cliente (Fk)
    val tipo: String,            // 'Compraventa' o 'Arrendamiento'
    val monto_total_final: String, // DecimalField viaja como String
    val fecha_firma: String,     // Date de Django viaja como String ("YYYY-MM-DD")
    val vigente: Boolean
) : Parcelable