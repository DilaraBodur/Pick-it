package de.syntax_institut.androidabschlussprojekt.features.game.domain.usecases

import de.syntax_institut.androidabschlussprojekt.features.game.data.models.Symbol

class CalculatePointsUseCase {

    fun calculatePoints(symbol: Symbol, sameSymbolCount: Int, round: Int): Int {
        val basePoints = symbol.basePoints

        val comboMultiplier = when (sameSymbolCount) {
            2 -> 0.8
            3 -> 1.0
            4 -> 1.5
            5 -> 2.0
            else -> 0.0
        }

        val roundMultiplier = when (round) {
            1 -> 1.0
            2 -> 1.2
            3 -> 1.4
            4 -> 1.6
            5 -> 1.8
            else -> 1.0
        }

        return (basePoints * comboMultiplier * roundMultiplier).toInt()
    }
}