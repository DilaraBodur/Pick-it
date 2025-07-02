package de.syntax_institut.androidabschlussprojekt.features.game.data.repositories

import de.syntax_institut.androidabschlussprojekt.features.game.data.models.SymbolPackage
import de.syntax_institut.androidabschlussprojekt.features.game.data.remote.SymbolsApi

class SymbolsRepository(private val api: SymbolsApi) {
    suspend fun loadSymbols(): List<SymbolPackage> = api.getSymbolPackages()
}