package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Zona(
    val id: Int,
    val nombre: String,
    val ciudad: String,
    val codigo_postal: String? = null // 👈 Soporta nulos como tu models.py
) : Parcelable