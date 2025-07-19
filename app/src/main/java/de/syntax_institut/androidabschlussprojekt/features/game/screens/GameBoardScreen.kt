package de.syntax_institut.androidabschlussprojekt.features.game.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import de.syntax_institut.androidabschlussprojekt.R
import de.syntax_institut.androidabschlussprojekt.features.game.composables.DrawButtons
import de.syntax_institut.androidabschlussprojekt.features.game.composables.ExitDialog
import de.syntax_institut.androidabschlussprojekt.features.game.composables.GameEndDialog
import de.syntax_institut.androidabschlussprojekt.features.game.composables.HeaderSection
import de.syntax_institut.androidabschlussprojekt.features.game.composables.MissionBoard
import de.syntax_institut.androidabschlussprojekt.features.game.composables.NextRoundDialog
import de.syntax_institut.androidabschlussprojekt.features.game.composables.SlotComposableWithReels
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel


@Composable
fun GameBoardScreen(
    navController: NavHostController,
    viewModel: GameViewModel,
    onExit: () -> Unit
) {
    val totalPoints = viewModel.totalPoints.collectAsState().value

    val showDialog by viewModel.showExitDialog.collectAsState()
    val showGameEndDialog by viewModel.showGameEndDialog.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startSpin(isAutoSpin = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.photo_2025_07_19_05_06_40),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
        ExitDialog(
            viewModel = viewModel,
            onExit = onExit
        )
    }

    if (viewModel.showNextRoundDialog.collectAsState().value) {
        NextRoundDialog(
            gameViewModel = viewModel,
            totalPoints = totalPoints
        )
    }

    if (showGameEndDialog) {
        LaunchedEffect(Unit) {
            viewModel.finishGameIfNeeded()
        }
        GameEndDialog(
            username = viewModel.authViewModel.currentUserModel.collectAsState().value?.username ?: "",
            totalPoints = totalPoints,
            onRestart = { viewModel.restartGame() },
            onExit = { navController.navigate("lobby") }
        )
    }
}