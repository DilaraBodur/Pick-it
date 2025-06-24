package de.syntax_institut.androidabschlussprojekt.data.remote

import de.syntax_institut.androidabschlussprojekt.data.model.UsernamesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UsernameApi {
    @GET("api/random-usernames")
    suspend fun getUsernames(
        @Query("count") count: Int = 1
    ): UsernamesResponse
}
