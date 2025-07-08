package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel

@Composable
fun RoundInfo(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    val round = viewModel.currentRound.collectAsState().value
    Text(
        text = "Runde $round / 5",
        fontSize = 16.sp,
        color = Color.White,
        modifier = modifier
    )
}