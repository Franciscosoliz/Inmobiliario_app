package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Propiedad(
    val id: Int,
    val titulo: String,
    val descripcion: String? = null, // Admite nulls en Django
    val tipo_inmueble: String,       // 'Casa', 'Departamento', etc.
    val estado_negocio: String,      // 'Disponible', 'Arrendado', 'Vendido'
    val precio: String,              // DecimalField viaja como String ("14000.00")
    val direccion: String,
    val habitaciones: Int,
    val banos: Int,
    val area_metros: String,         // DecimalField viaja como String
    val agente: Int,                 // ID del Agente (Fk)
    val zona: Int,                   // ID de la Zona (Fk)
    val imagen: String? = null,      // URL de la imagen en el Media de Django (puede ser null)

    // 💡 Campos extra: Si tu Serializer en Django incluye campos de lectura como
    // depth = 1 o SerializerMethodField para mostrar los nombres directamente:
    val agente_nombre: String? = null,
    val zona_nombre: String? = null
) : Parcelable