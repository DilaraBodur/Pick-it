package de.syntax_institut.androidabschlussprojekt.features.game.domain.usecases

import de.syntax_institut.androidabschlussprojekt.features.game.data.models.Symbol


class CalculatePointsUseCase {

    fun calculatePoints(
        symbol: Symbol,
        combinationType: String,
        round: Int
    ): Int {
        val specialPoints = when (combinationType.lowercase()) {
            "4er" -> 3200
            "5er" -> 4000
            "fullhouse" -> 3000
            "5verschiedene" -> 3000
            else -> symbol.basePoints
        }

        val roundMultiplier = when (round) {
            1 -> 1.0
            2 -> 1.2
            3 -> 1.4
            4 -> 1.6
            5 -> 1.8
            else -> 1.0
        }

        return (specialPoints * roundMultiplier).toInt()
    }

    fun calculateJokerPoints(round: Int): Int {
        val baseJokerPoints = 180
        val multiplier = when (round) {
            1 -> 1.0
            2 -> 1.2
            3 -> 1.4
            4 -> 1.6
            5 -> 1.8
            else -> 1.0
        }
        return (baseJokerPoints * multiplier).toInt()
    }
}