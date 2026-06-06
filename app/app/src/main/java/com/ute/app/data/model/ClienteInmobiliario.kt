package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ClienteInmobiliario(
    val id: Int,
    val nombre_completo: String, // 👈 CORREGIDO: Coincide con tu models.py
    val identificacion: String,   // Cédula o RUC
    val email: String,            // 👈 CORREGIDO: Volvemos a 'email' porque así está en Django
    val telefono: String,
    val presupuesto_maximo: String? = null // Los DecimalField de Django viajan como String en el JSON
) : Parcelable