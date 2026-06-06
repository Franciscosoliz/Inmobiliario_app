package com.ute.app.data

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val access: String,   
    val refresh: String?, 
    val is_staff: Boolean 
)