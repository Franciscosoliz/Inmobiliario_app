package com.ute.app.data

import com.ute.app.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // 🔐 --- AUTENTICACIÓN ---
    @POST("api/token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // 🏠 --- CRUD PROPIEDADES (CON MULTIPART PARA IMÁGENES) ---
    @GET("api/propiedades/")
    suspend fun getPropiedades(@Header("Authorization") token: String): Response<DjangoResponse<Propiedad>>

    @Multipart
    @POST("api/propiedades/")
    suspend fun crearPropiedadMultipart(
        @Header("Authorization") token: String,
        @Part("titulo") titulo: RequestBody,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("tipo_inmueble") tipoInmueble: RequestBody,
        @Part("estado_negocio") estadoNegocio: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("direccion") direccion: RequestBody,
        @Part("habitaciones") habitaciones: RequestBody,
        @Part("banos") banos: RequestBody,
        @Part("area_metros") areaMetros: RequestBody,
        @Part("agente") agenteId: RequestBody,
        @Part("zona") zonaId: RequestBody,
        @Part imagen: MultipartBody.Part? // Binario de la foto
    ): Response<Propiedad>

    @Multipart
    @PUT("api/propiedades/{id}/")
    suspend fun actualizarPropiedadMultipart(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("titulo") titulo: RequestBody,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("tipo_inmueble") tipoInmueble: RequestBody,
        @Part("estado_negocio") estadoNegocio: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("direccion") direccion: RequestBody,
        @Part("habitaciones") habitaciones: RequestBody,
        @Part("banos") banos: RequestBody,
        @Part("area_metros") areaMetros: RequestBody,
        @Part("agente") agenteId: RequestBody,
        @Part("zona") zonaId: RequestBody,
        @Part imagen: MultipartBody.Part? // Opcional si no se edita la foto
    ): Response<Propiedad>

    @DELETE("api/propiedades/{id}/")
    suspend fun eliminarPropiedad(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // 📍 --- CRUD ZONAS ---
    @GET("api/zonas/")
    suspend fun getZonas(@Header("Authorization") token: String): Response<DjangoResponse<Zona>>

    @POST("api/zonas/")
    suspend fun crearZona(@Header("Authorization") token: String, @Body zona: Zona): Response<Zona>

    @PUT("api/zonas/{id}/")
    suspend fun actualizarZona(@Header("Authorization") token: String, @Path("id") id: Int, @Body zona: Zona): Response<Zona>

    @DELETE("api/zonas/{id}/")
    suspend fun eliminarZona(@Header("Authorization") token: String, @Path("id") id: Int): Response<Unit>

    // 💼 --- CRUD AGENTES ---
    @GET("api/agentes/")
    suspend fun getAgentes(@Header("Authorization") token: String): Response<DjangoResponse<Agente>>

    @POST("api/agentes/")
    suspend fun crearAgente(@Header("Authorization") token: String, @Body agente: Agente): Response<Agente>

    @PUT("api/agentes/{id}/")
    suspend fun actualizarAgente(@Header("Authorization") token: String, @Path("id") id: Int, @Body agente: Agente): Response<Agente>

    @DELETE("api/agentes/{id}/")
    suspend fun eliminarAgente(@Header("Authorization") token: String, @Path("id") id: Int): Response<Unit>

    // 👥 --- CRUD CLIENTES ---
    @GET("api/clientes/")
    suspend fun getClientes(@Header("Authorization") token: String): Response<DjangoResponse<ClienteInmobiliario>>

    @POST("api/clientes/")
    suspend fun crearCliente(@Header("Authorization") token: String, @Body cliente: ClienteInmobiliario): Response<ClienteInmobiliario>

    @PUT("api/clientes/{id}/")
    suspend fun actualizarCliente(@Header("Authorization") token: String, @Path("id") id: Int, @Body cliente: ClienteInmobiliario): Response<ClienteInmobiliario>

    @DELETE("api/clientes/{id}/")
    suspend fun eliminarCliente(@Header("Authorization") token: String, @Path("id") id: Int): Response<Unit>

    // 📅 --- CRUD CITAS ---
    @GET("api/citas/")
    suspend fun getCitas(@Header("Authorization") token: String): Response<DjangoResponse<Cita>>

    @POST("api/citas/")
    suspend fun crearCita(@Header("Authorization") token: String, @Body cita: Cita): Response<Cita>

    @PUT("api/citas/{id}/")
    suspend fun actualizarCita(@Header("Authorization") token: String, @Path("id") id: Int, @Body cita: Cita): Response<Cita>

    @DELETE("api/citas/{id}/")
    suspend fun eliminarCita(@Header("Authorization") token: String, @Path("id") id: Int): Response<Unit>
}