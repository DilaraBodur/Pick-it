package de.syntax_institut.androidabschlussprojekt.features.user.data.models

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val loginProvider: String = "",
    val creationTimestamp: Timestamp = Timestamp.now(),
    val lastSignInTimestamp: Timestamp = Timestamp.now(),
    val countryCode: String = "",
    val friends: List<String> = emptyList(),
    val level: Int = 1,
    val totalPoints: Int = 0,
    val stats: PlayerStats = PlayerStats(),
    val purchasedPackages: List<String> = emptyList(),
    val activePackageId: String = "standard"
)
