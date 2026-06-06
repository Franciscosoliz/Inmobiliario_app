package com.ute.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ute.app.data.model.*
import com.ute.app.ui.login.LoginScreen
import com.ute.app.ui.login.LoginViewModel
import com.ute.app.ui.navigation.BottomNavigationBar
import com.ute.app.ui.propiedades.*
import com.ute.app.ui.gestion.*

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val loginViewModel: LoginViewModel = viewModel()

                // Estados globales de sesión
                var authToken by remember { mutableStateOf("") }
                var isStaff by remember { mutableStateOf(false) } // 👈 Control de acceso

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val showBars = currentRoute != "login"

                Scaffold(
                    topBar = {
                        if (showBars) {
                            TopAppBar(
                                title = { Text("Panel Inmobiliario") },
                                actions = {
                                    IconButton(onClick = {
                                        loginViewModel.cerrarSesion {
                                            authToken = ""
                                            isStaff = false
                                            navController.navigate("login") {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    }) {
                                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Cerrar", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = { if (showBars) BottomNavigationBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(viewModel = loginViewModel, onLoginSuccess = { token, staff ->
                                authToken = token
                                isStaff = staff
                                navController.navigate("propiedades") { popUpTo("login") { inclusive = true } }
                            })
                        }

                        composable("propiedades") {
                            PropiedadesScreen(
                                viewModel = viewModel(), token = authToken, isStaff = isStaff,
                                onPropiedadClick = { prop ->
                                    navController.currentBackStackEntry?.savedStateHandle?.set("propiedad_seleccionada", prop)
                                    navController.navigate("detalle_propiedad")
                                },
                                onAddClick = { navController.navigate("registrar_propiedad") },
                                onEditarClick = { prop ->
                                    navController.currentBackStackEntry?.savedStateHandle?.set("propiedad_a_editar", prop)
                                    navController.navigate("editar_propiedad")
                                }
                            )
                        }

                        composable("agentes") {
                            AgentesScreen(
                                viewModel = viewModel(), token = authToken, isStaff = isStaff,
                                onAddClick = { navController.navigate("registrar_agente") },
                                onEditarClick = { ag ->
                                    navController.currentBackStackEntry?.savedStateHandle?.set("agente_a_editar", ag)
                                    navController.navigate("editar_agente")
                                }
                            )
                        }

                        composable("clientes") {
                            ClientesScreen(
                                viewModel = viewModel(), token = authToken, isStaff = isStaff,
                                onEditarClick = { cli ->
                                    navController.currentBackStackEntry?.savedStateHandle?.set("cliente_a_editar", cli)
                                    navController.navigate("editar_cliente")
                                },
                                onAddClick = { navController.navigate("registrar_cliente") }
                            )
                        }

                        composable("zonas") {
                            ZonasScreen(
                                viewModel = viewModel(), token = authToken, isStaff = isStaff,
                                onEditarClick = { zona ->
                                    navController.currentBackStackEntry?.savedStateHandle?.set("zona_a_editar", zona)
                                    navController.navigate("editar_zona")
                                },
                                onAddClick = { navController.navigate("registrar_zona") }
                            )
                        }

                        composable("citas") {
                            CitasScreen(
                                viewModel = viewModel(), token = authToken, isStaff = isStaff,
                                onCrearCitaClick = { navController.navigate("registrar_cita") },
                                onEditarCitaClick = { cita ->
                                    navController.currentBackStackEntry?.savedStateHandle?.set("cita_seleccionada", cita)
                                    navController.navigate("editar_cita")
                                }
                            )
                        }

                        // Las rutas de edición/registro/detalle se mantienen igual...
                        // (Asegúrate de agregar aquí el resto de tus rutas de registro/edición)
                    }
                }
            }
        }
    }
}