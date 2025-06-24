package de.syntax_institut.androidabschlussprojekt.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import de.syntax_institut.androidabschlussprojekt.data.model.User
import kotlinx.coroutines.tasks.await


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

    suspend fun getUserById(uid: String): User? {
        return try {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .await()
                .toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
}