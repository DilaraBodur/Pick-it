package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel

@Composable
fun SlotComposableWithReels(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    val currentReels by viewModel.currentReels.collectAsState()
    val isSpinning by viewModel.isSpinning.collectAsState()

    val backgroundColor = Color(0xFF1565C0)

    val lastIndex = currentReels.lastIndex

    LaunchedEffect(isSpinning) {
        if (!isSpinning) {
            viewModel.setSpinFinished()
            viewModel.evaluateCombination()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            currentReels.forEachIndexed { index, reel ->
                val isHeld = viewModel.heldSymbols.collectAsState().value.contains(index)

                if (isSpinning) {
                    SpinningReel(
                        symbols = reel,
                        isSpinning = true,
                        spinDuration = 3000,
                        isHeld = isHeld,
                        onToggleHold = { viewModel.toggleHold(index) }
                    )
                } else {
                    SlotReel(
                        reelSymbols = reel,
                        isHeld = isHeld,
                        onToggleHold = { viewModel.toggleHold(index) }
                    )
                }

                if (index < lastIndex) {
                    DiamondShape(
                        size = 14.dp,
                        color = Color.White
                    )
                }
            }
        }
    }
}