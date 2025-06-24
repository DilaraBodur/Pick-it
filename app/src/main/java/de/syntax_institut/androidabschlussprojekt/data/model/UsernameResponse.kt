package de.syntax_institut.androidabschlussprojekt.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsernamesResponse(
    val usernames: List<String>
)
