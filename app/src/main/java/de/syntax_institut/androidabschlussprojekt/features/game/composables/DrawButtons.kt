package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val spinCount by viewModel.spinCount.collectAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal =48.dp),
        horizontalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { viewModel.startSpin() },
            enabled = spinCount < 1,
            modifier = Modifier.height(40.dp).weight(1f).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Draw")
        }

        Button(
            onClick = { viewModel.startSpin() },
            enabled = spinCount < 2,
            modifier = Modifier.height(40.dp).weight(1f).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Draw")
        }
    }
}