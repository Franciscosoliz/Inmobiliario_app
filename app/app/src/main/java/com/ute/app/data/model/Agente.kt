package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Agente(
    val id: Int,
    val user: Int,
    val user_detail: UserDetail? = null,
    val licencia_profesional: String,
    val telefono: String,
    val especialidad: String,
    val activo: Boolean
) : Parcelable

@Parcelize
@Serializable
data class UserDetail(
    val id: Int,
    val username: String,
    val email: String,
    val is_staff: Boolean
) : Parcelable