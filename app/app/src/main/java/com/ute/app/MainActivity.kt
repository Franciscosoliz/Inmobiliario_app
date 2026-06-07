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

                var authToken by remember { mutableStateOf("") }
                var isStaff by remember { mutableStateOf(false) }

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

                        composable("detalle_propiedad") {
                            val prop = navController.previousBackStackEntry?.savedStateHandle?.get<Propiedad>("propiedad_seleccionada")
                            if (prop != null) {
                                DetallePropiedadScreen(propiedad = prop, onBackClick = { navController.popBackStack() })
                            }
                        }

                        composable("editar_propiedad") {
                            val prop = navController.previousBackStackEntry?.savedStateHandle?.get<Propiedad>("propiedad_a_editar")
                            if (prop != null) {
                                EditarPropiedadScreen(viewModel = viewModel(), propiedadAEditar = prop, token = authToken, onBackClick = { navController.popBackStack() })
                            }
                        }

                        // --- REGISTRO ---
                        composable("registrar_propiedad") { CrearPropiedadScreen(viewModel = viewModel(), token = authToken, onBackClick = { navController.popBackStack() }) }
                        composable("registrar_agente") { CrearAgenteScreen(viewModel = viewModel(), token = authToken, onBackClick = { navController.popBackStack() }) }
                        composable("registrar_cliente") { CrearClienteScreen(viewModel = viewModel(), token = authToken, onBackClick = { navController.popBackStack() }) }
                        composable("registrar_zona") { CrearZonaScreen(viewModel = viewModel(), token = authToken, onBackClick = { navController.popBackStack() }) }
                        composable("registrar_cita") { CrearCitaScreen(viewModel = viewModel(), token = authToken, onBackClick = { navController.popBackStack() }) }

                        // --- EDICIÓN ---
                        composable("editar_agente") {
                            val ag = navController.previousBackStackEntry?.savedStateHandle?.get<Agente>("agente_a_editar")
                            if (ag != null) { EditarAgenteScreen(viewModel = viewModel(), agente = ag, token = authToken, onBackClick = { navController.popBackStack() }) }
                        }
                        composable("editar_cliente") {
                            val cli = navController.previousBackStackEntry?.savedStateHandle?.get< ClienteInmobiliario>("cliente_a_editar")
                            if (cli != null) { EditarClienteScreen(viewModel = viewModel(), cliente = cli, token = authToken, onBackClick = { navController.popBackStack() }) }
                        }
                        composable("editar_zona") {
                            val zona = navController.previousBackStackEntry?.savedStateHandle?.get<Zona>("zona_a_editar")
                            if (zona != null) { EditarZonaScreen(viewModel = viewModel(), zona = zona, token = authToken, onBackClick = { navController.popBackStack() }) }
                        }
                        composable("editar_cita") {
                            val cita = navController.previousBackStackEntry?.savedStateHandle?.get<Cita>("cita_seleccionada")
                            if (cita != null) { EditarCitaScreen(viewModel = viewModel(), citaAEditar = cita, token = authToken, onBackClick = { navController.popBackStack() }) }
                        }
                    }
                }
            }
        }
    }
}