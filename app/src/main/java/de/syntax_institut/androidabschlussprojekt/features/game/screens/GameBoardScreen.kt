package de.syntax_institut.androidabschlussprojekt.features.game.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import de.syntax_institut.androidabschlussprojekt.features.game.composables.DrawButtons
import de.syntax_institut.androidabschlussprojekt.features.game.composables.GameEndDialog
import de.syntax_institut.androidabschlussprojekt.features.game.composables.HeaderSection
import de.syntax_institut.androidabschlussprojekt.features.game.composables.MissionBoard
import de.syntax_institut.androidabschlussprojekt.features.game.composables.SlotComposableWithReels
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel


@Composable
fun GameBoardScreen(
    navController: NavHostController,
    viewModel: GameViewModel,
    onExit: () -> Unit
) {
    val backgroundColor = Color(0xFF083A8C)

    val currentPoints = viewModel.currentPoints.collectAsState().value
    val roundBonus = viewModel.roundBonus.collectAsState().value
    val displayedPoints = currentPoints + roundBonus
    val totalPoints = viewModel.totalPoints.collectAsState().value

    val showDialog by viewModel.showExitDialog.collectAsState()
    val showGameEndDialog by viewModel.showGameEndDialog.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startSpin(isAutoSpin = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                HeaderSection(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                )

                SlotComposableWithReels(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                DrawButtons(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )
            }

            MissionBoard(
                viewModel = viewModel,
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            )
        }
    }
    if (showDialog) {
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

    if (viewModel.showNextRoundDialog.collectAsState().value) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text("Runde beendet")
            },
            text = {
                Column {
                    Text("Spieler: ${viewModel.authViewModel.currentUserModel.value?.username ?: "?"}")
                    Text("Punkte: $displayedPoints")
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.closeNextRoundDialogAndStart() }) {
                    Text("Nächste Runde")
                }
            }
        )
    }

    if (showGameEndDialog) {
        GameEndDialog(
            username = viewModel.authViewModel.currentUserModel.collectAsState().value?.username ?: "",
            totalPoints = totalPoints,
            onRestart = { viewModel.closeGameEndDialog() },
            onExit = { navController.navigate("lobby") }
        )
    }
}