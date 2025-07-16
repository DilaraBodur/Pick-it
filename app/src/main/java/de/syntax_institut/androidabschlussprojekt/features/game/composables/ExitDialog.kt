package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel

@Composable
fun ExitDialog(
    viewModel: GameViewModel,
    onExit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { viewModel.closeExitDialog() },
        title = { Text("Spiel beenden?") },
        text = { Text("Möchtest du das Spiel wirklich verlassen? Alle Punkte gehen verloren.") },
        confirmButton = {
            Button(onClick = {
                viewModel.exitGame()
                onExit()
            }) {
                Text("Beenden")
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.closeExitDialog() }) {
                Text("Abbrechen")
            }
        }
    )
}