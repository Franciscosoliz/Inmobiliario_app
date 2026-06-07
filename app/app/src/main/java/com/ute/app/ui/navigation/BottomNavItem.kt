package com.ute.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.DateRange

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {

    object Propiedades : BottomNavItem(
        route = "propiedades",
        title = "Propiedades",
        icon = Icons.Default.Home
    )

    object Agentes : BottomNavItem(
        route = "agentes",
        title = "Agentes",
        icon = Icons.Default.Person
    )

    object Zonas : BottomNavItem(
        route = "zonas",
        title = "Zonas",
        icon = Icons.Default.LocationOn
    )

    object Clientes : BottomNavItem(
        route = "clientes",
        title = "Clientes",
        icon = Icons.Default.AccountCircle
    )

    object CerrarSesion : BottomNavItem(
        route = "logout",
        title = "Salir",
        icon = Icons.Default.ExitToApp
    )

    object Citas : BottomNavItem(
        route = "citas",
        title = "Citas",
        icon = Icons.Default.DateRange
    )
}