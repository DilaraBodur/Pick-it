package de.syntax_institut.androidabschlussprojekt.features.user.data.repositories

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import de.syntax_institut.androidabschlussprojekt.features.user.data.models.User
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

    fun addFriend(userId: String, friendId: String) {
        usersCollection
            .document(userId)
            .update("friends", FieldValue.arrayUnion(friendId))
    }

    fun getUserByUsername(username: String, onResult: (User?) -> Unit) {
        usersCollection
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { result ->
                val user = result.documents.firstOrNull()?.toObject(User::class.java)
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
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

    suspend fun getPurchasedPackageIds(userId: String): List<String> {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .get()
            .await()

        val rawList = snapshot.get("purchasedPackages")

        return if (rawList is List<*>) {
            rawList.filterIsInstance<String>()
        } else {
            emptyList()
        }
    }

    suspend fun addPurchasedPackageId(userId: String, newPackageId: String) {
        val firestore = FirebaseFirestore.getInstance()
        val userRef = firestore.collection("users").document(userId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val current = snapshot.get("purchasedPackages")
            val currentList = if (current is List<*>) {
                current.filterIsInstance<String>()
            } else {
                emptyList()
            }

            if (!currentList.contains(newPackageId)) {
                transaction.update(userRef, "purchasedPackages", currentList + newPackageId)
            }
        }.await()
    }
}