package de.syntax_institut.androidabschlussprojekt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.syntax_institut.androidabschlussprojekt.ui.screen.LoadingScreen
import de.syntax_institut.androidabschlussprojekt.ui.screen.LobbyScreen
import de.syntax_institut.androidabschlussprojekt.ui.screen.LoginScreen
import de.syntax_institut.androidabschlussprojekt.viewmodels.AuthViewModel

@Composable
fun AppStart(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.currentUser.collectAsState()
    var showNavHost by remember { mutableStateOf(false) }

    if (!showNavHost) {
        LoadingScreen {
            showNavHost = true
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = if (user != null) "lobby" else "login"
        ) {
            composable("login") {
                LoginScreen(
                    authViewModel = authViewModel,
                    navController = navController
                )
            }
            composable("lobby") {
                LobbyScreen(
                    authViewModel = authViewModel,
                    navController = navController
                )
            }
        }
    }
}