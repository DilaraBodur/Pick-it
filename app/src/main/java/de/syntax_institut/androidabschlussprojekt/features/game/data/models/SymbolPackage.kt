package de.syntax_institut.androidabschlussprojekt.features.game.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SymbolPackage(
    val packageId: String,
    val name: String,
    val price: Double,
    val symbols: List<Symbol>
)

@JsonClass(generateAdapter = true)
data class Symbol(
    val id: Int,
    val emoji: String,
    val name: String,
    val basePoints: Int
)