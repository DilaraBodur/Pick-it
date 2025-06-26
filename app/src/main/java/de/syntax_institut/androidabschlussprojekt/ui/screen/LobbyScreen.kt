package de.syntax_institut.androidabschlussprojekt.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import de.syntax_institut.androidabschlussprojekt.ui.components.AvatarImage
import de.syntax_institut.androidabschlussprojekt.viewmodels.AuthViewModel

@Composable
fun LobbyScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val userModel by authViewModel.currentUserModel.collectAsState()

    val username = when {
        !userModel?.username.isNullOrBlank() -> userModel?.username
        else -> "Unbekannt"
    }

    val avatarUrl = userModel?.photoUrl

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Du bist eingeloggt, $username!")

            Spacer(modifier = Modifier.width(8.dp))

            if (!avatarUrl.isNullOrBlank()) {
                AvatarImage(url = avatarUrl)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            authViewModel.logout()
            navController.navigate("login") {
                popUpTo("lobby") { inclusive = true }
            }
        }) {
            Text(text = "Logout")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showDeleteDialog = true }) {
            Text(text = "Konto löschen")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konto wirklich löschen?") },
            text = { Text("Dieser Vorgang kann nicht rückgängig gemacht werden.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    authViewModel.deleteAccount()
                    navController.navigate("login") {
                        popUpTo("lobby") { inclusive = true }
                    }
                }) {
                    Text("Löschen", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }
}



