package de.syntax_institut.androidabschlussprojekt.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.syntax_institut.androidabschlussprojekt.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onLoginSuccess()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Willkommen! Melde dich an", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(24.dp))

        Button(onClick = { authViewModel.loginAnonym() }) {
            Text("Als Gast fortfahren")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = { /* TODO: Google Login */ }) {
            Text("Mit Google anmelden")
        }

        Button(onClick = { /* TODO: Facebook Login */ }) {
            Text("Mit Facebook anmelden")
        }
    }
}