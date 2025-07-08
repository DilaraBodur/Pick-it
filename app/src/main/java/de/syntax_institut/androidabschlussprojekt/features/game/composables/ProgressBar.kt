package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel

@Composable
fun ProgressBar(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    val points = viewModel.totalPoints.collectAsState().value
    val progress = (points / 150000f).coerceIn(0f, 1f)

    LinearProgressIndicator(
        progress = progress,
        color = Color.Green,
        trackColor = Color.Gray,
        modifier = modifier
    )
}