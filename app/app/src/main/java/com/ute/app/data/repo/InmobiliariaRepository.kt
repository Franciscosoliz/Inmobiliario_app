package com.ute.app.data.repo

import com.ute.app.data.remote.RetrofitClient
import com.ute.app.data.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class InmobiliariaRepository {

    private val api = RetrofitClient.apiService

    // Obtener propiedades
    suspend fun obtenerPropiedades(token: String): Result<List<Propiedad>> {
        return try {
            val respuesta = api.getPropiedades("Bearer $token")
            if (respuesta.isSuccessful && respuesta.body() != null) {
                Result.success(respuesta.body()!!.results)
            } else {
                Result.failure(Exception("Error del servidor: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ➕ GUARDAR PROPIEDAD ADJUNTANDO IMAGEN (Soporta Crear y Editar)
    suspend fun guardarPropiedadConImagen(
        token: String,
        propiedad: Propiedad,
        imagenPart: MultipartBody.Part?,
        esEdicion: Boolean
    ): Result<Propiedad> {
        return try {
            // Transformamos datos primitivos a RequestBody de texto plano para Multipart
            val tituloBody = propiedad.titulo.toRequestBody("text/plain".toMediaTypeOrNull())
            val descBody = propiedad.descripcion?.toRequestBody("text/plain".toMediaTypeOrNull())
            val tipoBody = propiedad.tipo_inmueble.toRequestBody("text/plain".toMediaTypeOrNull())
            val estadoBody = propiedad.estado_negocio.toRequestBody("text/plain".toMediaTypeOrNull())
            val precioBody = propiedad.precio.toRequestBody("text/plain".toMediaTypeOrNull())
            val dirBody = propiedad.direccion.toRequestBody("text/plain".toMediaTypeOrNull())
            val habBody = propiedad.habitaciones.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val banosBody = propiedad.banos.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val areaBody = propiedad.area_metros.toRequestBody("text/plain".toMediaTypeOrNull())
            val agenteBody = propiedad.agente.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val zonaBody = propiedad.zona.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val respuesta = if (esEdicion) {
                api.actualizarPropiedadMultipart(
                    token = "Bearer $token", id = propiedad.id, titulo = tituloBody, descripcion = descBody,
                    tipoInmueble = tipoBody, estadoNegocio = estadoBody, precio = precioBody, direccion = dirBody,
                    habitaciones = habBody, banos = banosBody, areaMetros = areaBody, agenteId = agenteBody,
                    zonaId = zonaBody, imagen = imagenPart
                )
            } else {
                api.crearPropiedadMultipart(
                    token = "Bearer $token", titulo = tituloBody, descripcion = descBody,
                    tipoInmueble = tipoBody, estadoNegocio = estadoBody, precio = precioBody, direccion = dirBody,
                    habitaciones = habBody, banos = banosBody, areaMetros = areaBody, agenteId = agenteBody,
                    zonaId = zonaBody, imagen = imagenPart
                )
            }

            if (respuesta.isSuccessful && respuesta.body() != null) {
                Result.success(respuesta.body()!!)
            } else {
                Result.failure(Exception("Error en servidor Django: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar propiedad
    suspend fun eliminarPropiedad(token: String, id: Int): Result<Unit> {
        return try {
            val respuesta = api.eliminarPropiedad("Bearer $token", id)
            if (respuesta.isSuccessful) Result.success(Unit) else Result.failure(Exception("Error al eliminar"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener catálogos secundarios
    suspend fun obtenerZonas(token: String): Result<List<Zona>> {
        return try {
            val respuesta = api.getZonas("Bearer $token")
            if (respuesta.isSuccessful && respuesta.body() != null) Result.success(respuesta.body()!!.results)
            else Result.failure(Exception("Error"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun obtenerAgentes(token: String): Result<List<Agente>> {
        return try {
            val respuesta = api.getAgentes("Bearer $token")
            if (respuesta.isSuccessful && respuesta.body() != null) Result.success(respuesta.body()!!.results)
            else Result.failure(Exception("Error"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun crearAgente(token: String, agente: Agente): Result<Agente> {
        return try {
            val response = api.crearAgente("Bearer $token", agente)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Error al crear: ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun actualizarAgente(token: String, id: Int, agente: Agente): Result<Agente> {
        return try {
            val response = api.actualizarAgente("Bearer $token", id, agente)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Error al actualizar: ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun eliminarAgente(token: String, id: Int): Result<Unit> {
        return try {
            val response = api.eliminarAgente("Bearer $token", id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al eliminar"))
        } catch (e: Exception) { Result.failure(e) }
    }
}