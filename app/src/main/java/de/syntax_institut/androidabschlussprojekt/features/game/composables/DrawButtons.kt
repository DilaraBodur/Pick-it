package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel

@Composable
fun DrawButtons(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    val isSpinning by viewModel.isSpinning.collectAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { viewModel.startSpin() },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(text = if (isSpinning) "..." else "Draw")
        }

        Button(
            onClick = { viewModel.evaluateCombination() },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(text = "Kombination prüfen")
        }
    }
}