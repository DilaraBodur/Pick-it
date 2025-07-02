package de.syntax_institut.androidabschlussprojekt.features.user.data.repositories

import de.syntax_institut.androidabschlussprojekt.features.user.remote.UsernameApi

class UsernameRepository(
    private val api: UsernameApi
) {
    suspend fun getRandomUsername(): String {
        return try {
            val response = api.getUsernames()
            response.usernames.firstOrNull() ?: ("Player" + (1000..9999).random())
        } catch (e: Exception) {
            "Player" + (1000..9999).random()
        }
    }
}