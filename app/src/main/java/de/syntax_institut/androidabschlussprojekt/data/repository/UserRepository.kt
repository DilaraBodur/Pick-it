package de.syntax_institut.androidabschlussprojekt.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import de.syntax_institut.androidabschlussprojekt.data.model.User


class UserRepository {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    fun saveUser(user: User, onSuccess: () -> Unit = {}, onError: (Exception) -> Unit = {}) {
        usersCollection.document(user.uid).set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteUser(uid: String) {
        usersCollection.document(uid).delete()
    }
}