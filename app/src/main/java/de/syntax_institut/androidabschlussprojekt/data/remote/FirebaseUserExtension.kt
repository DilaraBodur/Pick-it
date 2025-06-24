package de.syntax_institut.androidabschlussprojekt.data.remote

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.Timestamp
import de.syntax_institut.androidabschlussprojekt.data.model.User

fun FirebaseUser.toUserModel(): User {
    return User(
        uid = uid,
        username = displayName ?: ("User" + uid.take(5)),
        displayName = displayName ?: "",
        email = email ?: "",
        photoUrl = photoUrl?.toString(),
        loginProvider = providerData.firstOrNull { it.providerId != "firebase" }?.providerId ?: "firebase",
        creationTimestamp = metadata?.creationTimestamp?.let {
            Timestamp(it / 1000, 0)
        } ?: Timestamp.now(),
        lastSignInTimestamp = metadata?.lastSignInTimestamp?.let {
            Timestamp(it / 1000, 0)
        } ?: Timestamp.now()
    )
}