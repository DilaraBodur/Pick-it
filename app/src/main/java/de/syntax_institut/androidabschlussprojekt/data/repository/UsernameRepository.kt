package de.syntax_institut.androidabschlussprojekt.data.repository

import de.syntax_institut.androidabschlussprojekt.data.remote.UsernameApi

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