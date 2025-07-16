package de.syntax_institut.androidabschlussprojekt.features.game.composables


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel

@Composable
fun NextRoundDialog(
    gameViewModel: GameViewModel,
    totalPoints: Int
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text("Runde beendet")
        },
        text = {
            Column {
                Text("Spieler: ${gameViewModel.authViewModel.currentUserModel.value?.username ?: "?"}")
                Text("Punkte: $totalPoints")
            }
        },
        confirmButton = {
            Button(onClick = { gameViewModel.closeNextRoundDialogAndStart() }) {
                Text("Nächste Runde")
            }
        }
    )
}


