package com.example.praktikum2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.praktikum2.navigation.AppBottomNavigation
import com.example.praktikum2.navigation.DrawerContent
import com.example.praktikum2.navigation.Screen
import com.example.praktikum2.ui.theme.ShoppingListTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            ShoppingListTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val subScreens = listOf(
                    Screen.ChangePassword.route,
                    Screen.PrivacyPolicy.route,
                    Screen.HelpCenter.route,
                    Screen.About.route
                )

                val currentScreenTitle = when (currentRoute) {
                    Screen.Home.route -> "Shopping List"
                    Screen.Profile.route -> "Profile"
                    Screen.Settings.route -> "Settings"
                    Screen.ChangePassword.route -> "Ganti Kata Sandi"
                    Screen.PrivacyPolicy.route -> "Kebijakan Privasi"
                    Screen.HelpCenter.route -> "Pusat Bantuan"
                    Screen.About.route -> "Tentang Aplikasi"
                    else -> "Shopping App"
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(navController = navController, closeDrawer = {
                            scope.launch { drawerState.close() }
                        })
                    }
                ) {
                    Scaffold(
                        topBar = {
                            if (currentRoute != Screen.Profile.route) {
                                TopAppBar(
                                    title = { Text(currentScreenTitle) },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            if (currentRoute in subScreens) {
                                                navController.popBackStack()
                                            } else {
                                                scope.launch { drawerState.open() }
                                            }
                                        }) {
                                            Icon(
                                                if (currentRoute in subScreens) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Menu,
                                                contentDescription = "Menu"
                                            )
                                        }
                                    },
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        },
                        bottomBar = {
                            if (currentRoute !in subScreens) {
                                AppBottomNavigation(navController = navController)
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            enterTransition = { slideInHorizontally { it } + fadeIn() },
                            exitTransition = { slideOutHorizontally { -it } + fadeOut() },
                            popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
                            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
                        ) {
                            composable(Screen.Home.route) { ShoppingListScreen() }
                            composable(Screen.Profile.route) { ProfileScreen() }
                            composable(Screen.Settings.route) {
                                SettingsScreen(
                                    isDarkTheme = isDarkTheme,
                                    onThemeChange = { isDarkTheme = it },
                                    navController = navController
                                )
                            }
                            composable(Screen.ChangePassword.route) { ChangePasswordScreen() }
                            composable(Screen.PrivacyPolicy.route) { PrivacyPolicyScreen() }
                            composable(Screen.HelpCenter.route) { HelpCenterScreen() }

                            // PASTIKAN BARIS INI ADA
                            composable(Screen.About.route) { AboutScreen() }
                        }
                    }
                }
            }
        }
    }
}