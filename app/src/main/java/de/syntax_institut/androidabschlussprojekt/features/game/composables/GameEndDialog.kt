package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameEndDialog(
    username: String,
    totalPoints: Int,
    onRestart: () -> Unit,
    onExit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Spiel beendet") },
        text = {
            Column {
                Text("Spieler: $username")
                Text("Deine Gesamtpunktzahl: $totalPoints")
            }
        },
        confirmButton = {
            Button(onClick = onRestart) {
                Text("Neustart")
            }
        },
        dismissButton = {
            Button(onClick = onExit) {
                Text("Zurück zur Lobby")
            }
        }
    )
}