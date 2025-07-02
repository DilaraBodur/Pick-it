package de.syntax_institut.androidabschlussprojekt.features.game.data.remote

import de.syntax_institut.androidabschlussprojekt.features.game.data.models.SymbolPackage
import retrofit2.http.GET

interface SymbolsApi {
    @GET("symbols.json")
    suspend fun getSymbolPackages(): List<SymbolPackage>
}