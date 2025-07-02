package de.syntax_institut.androidabschlussprojekt.features.user.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsernamesResponse(
    val usernames: List<String>
)
