package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel


@Composable
fun HeaderSection(
    viewModel: GameViewModel,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {

    val buttonWidth = 150.dp

    val headerButtonSizeModifier = Modifier
        .width(buttonWidth)
        .height(56.dp)
        .padding(horizontal = 12.dp, vertical = 6.dp)

    val bonusProgress = viewModel.bonusProgress.collectAsState().value
    val currentPoints = viewModel.currentPoints.collectAsState().value
    val requiredPoints = viewModel.requiredPoints.collectAsState().value


    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ExitButton(onExit = onExit, modifier = headerButtonSizeModifier)

            TimeProgressBar(
                progress = viewModel.timeProgress.collectAsState().value,
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
                    .padding(start = 8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = headerButtonSizeModifier
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Runde ${viewModel.currentRound.collectAsState().value} / 5",
                    color = Color.White
                )
            }

            BonusProgressBar(
                progress = bonusProgress,
                currentPoints = currentPoints,
                requiredPoints = requiredPoints,
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}