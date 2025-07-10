package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.Symbol
import kotlinx.coroutines.delay

@Composable
fun SpinningReel(
    symbols: List<Symbol>,
    isSpinning: Boolean,
    spinDuration: Int = 2000
) {
    var displayedSymbols by remember { mutableStateOf(symbols) }

    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < spinDuration) {
                displayedSymbols = List(3) { symbols.random() }
                delay(100)
            }
        }
    }
    SlotReel(reelSymbols = displayedSymbols)
}