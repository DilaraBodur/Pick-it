package de.syntax_institut.androidabschlussprojekt

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.syntax_institut.androidabschlussprojekt.ui.screen.LobbyScreen
import de.syntax_institut.androidabschlussprojekt.ui.screen.LoginScreen
import de.syntax_institut.androidabschlussprojekt.viewmodels.AuthViewModel

@Composable
fun AppStart(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.currentUser.collectAsState()
    val isChecking by authViewModel.isChecking.collectAsState()

    if (isChecking) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
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

    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("lobby") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("lobby") { inclusive = true }
            }
        }
    }
}