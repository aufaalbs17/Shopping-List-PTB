package com.example.praktikum2.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

// Sealed class untuk mendefinisikan semua rute layar di aplikasi
sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    object Home : Screen("home", "Shopping List", Icons.Default.Home)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object ChangePassword : Screen("change_password", "Ganti Kata Sandi")
    object PrivacyPolicy : Screen("privacy_policy", "Kebijakan Privasi")
    object HelpCenter : Screen("help_center", "Pusat Bantuan")
    object About : Screen("about", "Tentang Aplikasi")
}

// Daftar item yang akan ditampilkan di Bottom Navigation Bar
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Profile
)

// Composable untuk Bottom Navigation Bar
@Composable
fun AppBottomNavigation(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Logika untuk membersihkan back stack dan kembali ke halaman utama
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// Composable untuk konten di dalam Navigation Drawer
@Composable
fun DrawerContent(navController: NavController, closeDrawer: () -> Unit) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { Icon(Screen.Settings.icon!!, contentDescription = Screen.Settings.title) },
            label = { Text(Screen.Settings.title) },
            selected = false,
            onClick = {
                navController.navigate(Screen.Settings.route)
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}