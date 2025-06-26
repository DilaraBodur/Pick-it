package de.syntax_institut.androidabschlussprojekt.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import de.syntax_institut.androidabschlussprojekt.data.model.User

fun FirebaseUser.toUserModel(username: String, photoUrl: String?): User {
    val metadata = this.metadata
    val actualProviderId = this.providerData.find {
        it.providerId != "firebase"
    }?.providerId ?: "firebase"

    return User(
        uid = uid,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl,
        loginProvider = actualProviderId,
        username = username,
        creationTimestamp = metadata?.creationTimestamp?.let { Timestamp(it / 1000, 0) } ?: Timestamp.now(),
        lastSignInTimestamp = metadata?.lastSignInTimestamp?.let { Timestamp(it / 1000, 0) } ?: Timestamp.now()
    )
}