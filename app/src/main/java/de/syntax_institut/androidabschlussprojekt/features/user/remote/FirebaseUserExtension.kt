package de.syntax_institut.androidabschlussprojekt.features.user.remote

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import de.syntax_institut.androidabschlussprojekt.features.user.data.models.PlayerStats
import de.syntax_institut.androidabschlussprojekt.features.user.data.models.User

fun FirebaseUser.toUserModel(
    username: String,
    fullName: String,
    email: String?,
    photoUrl: String?,
    level: Int = 1,
    totalPoints: Int = 0,
    countryCode: String,
    stats: PlayerStats = PlayerStats(),
    purchasedPackages: List<String> = emptyList()
): User {
    val metadata = this.metadata
    val actualProviderId = this.providerData.find {
        it.providerId != "firebase"
    }?.providerId ?: "firebase"

    return User(
        uid = uid,
        email = email,
        displayName = fullName,
        photoUrl = photoUrl,
        loginProvider = actualProviderId,
        username = username,
        creationTimestamp = metadata?.creationTimestamp?.let { Timestamp(it / 1000, 0) } ?: Timestamp.now(),
        lastSignInTimestamp = metadata?.lastSignInTimestamp?.let { Timestamp(it / 1000, 0) } ?: Timestamp.now(),
        level = level,
        totalPoints = totalPoints,
        countryCode = countryCode,
        stats = stats,
        purchasedPackages = purchasedPackages
    )
}