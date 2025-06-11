package de.syntax_institut.androidabschlussprojekt

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.syntax_institut.androidabschlussprojekt.ui.screen.LobbyScreen
import de.syntax_institut.androidabschlussprojekt.ui.screen.LoginScreen
import de.syntax_institut.androidabschlussprojekt.viewmodels.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppStart(
    authViewModel: AuthViewModel = koinViewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        if (isLoggedIn) {
            NavHost(
                navController = navController,
                startDestination = "lobby",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("lobby") {
                    LobbyScreen()
                }
                // weitere Screens wie "profil", "einstellungen"  hier hinzufügen später
            }
        } else {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("lobby") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}