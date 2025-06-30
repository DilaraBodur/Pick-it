package de.syntax_institut.androidabschlussprojekt.data.model


data class PlayerStats(
    val winRate: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val twoPlayerWins: Int = 0,
    val threePlayerWins: Int = 0,
    val fourPlayerWins: Int = 0,
    val twoVsTwoWins: Int = 0
)