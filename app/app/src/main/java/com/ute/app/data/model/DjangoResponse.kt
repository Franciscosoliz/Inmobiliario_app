package com.ute.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DjangoResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T> // 👈 Aquí se almacena la lista real de datos (como las propiedades)
)