package com.ute.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey // IMPORTANTE
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {
    
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val IS_STAFF_KEY = booleanPreferencesKey("is_staff") // Nueva clave

    // Guardar ambos datos a la vez
    suspend fun saveUserData(token: String, isStaff: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[IS_STAFF_KEY] = isStaff
        }
    }

    // Flujos para leer los datos
    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    
    val isStaffFlow: Flow<Boolean> = context.dataStore.data.map { it[IS_STAFF_KEY] ?: false }

    // Limpiar sesión completa
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}