package de.syntax_institut.androidabschlussprojekt.features.game.data.models


data class MissionItem(
    val id: String,
    val type: MissionType,
    val symbol: Symbol?,
    val isCompleted: Boolean,
    var basePoints: Int = 0,
    var isClaimed: Boolean = false
)

enum class MissionType {
    THREE, FOUR, FIVE, FULLHOUSE, FIVE_DIFF, JOKER
}