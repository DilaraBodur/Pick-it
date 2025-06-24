package de.syntax_institut.androidabschlussprojekt.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val loginProvider: String = "",
    val creationTimestamp: Timestamp = Timestamp.now(),
    val lastSignInTimestamp: Timestamp = Timestamp.now()
)
