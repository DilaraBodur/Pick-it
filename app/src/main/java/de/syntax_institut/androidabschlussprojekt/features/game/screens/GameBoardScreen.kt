package de.syntax_institut.androidabschlussprojekt.features.game.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.syntax_institut.androidabschlussprojekt.features.game.composables.DrawButtons
import de.syntax_institut.androidabschlussprojekt.features.game.composables.HeaderSection
import de.syntax_institut.androidabschlussprojekt.features.game.composables.MissionBoard
import de.syntax_institut.androidabschlussprojekt.features.game.composables.SlotComposableWithReels
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel


@Composable
fun GameBoardScreen(
    viewModel: GameViewModel,
    onExit: () -> Unit
) {
    val backgroundColor = Color(0xFF083A8C)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                HeaderSection(
                    viewModel = viewModel,
                    onExit = onExit,
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
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            MissionBoard(
                viewModel = viewModel,
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            )
        }
    }
}