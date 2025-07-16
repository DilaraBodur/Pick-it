package de.syntax_institut.androidabschlussprojekt.features.user.data.repositories

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import de.syntax_institut.androidabschlussprojekt.features.user.data.models.User
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")


    suspend fun addFriendBothWays(userId: String, friendId: String) {
        val userRef = usersCollection.document(userId)
        val friendRef = usersCollection.document(friendId)

        FirebaseFirestore.getInstance().runBatch { batch ->
            batch.update(userRef, "friends", FieldValue.arrayUnion(friendId))
            batch.update(friendRef, "friends", FieldValue.arrayUnion(userId))
        }.await()
    }

    fun saveUser(user: User, onSuccess: () -> Unit = {}, onError: (Exception) -> Unit = {}) {
        usersCollection.document(user.uid).set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteUser(uid: String) {
        usersCollection.document(uid).delete()
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

    suspend fun removeFriendBothWays(userId: String, friendId: String) {
        val userRef = usersCollection.document(userId)
        val friendRef = usersCollection.document(friendId)

        FirebaseFirestore.getInstance().runTransaction { transaction ->
            val userSnapshot = transaction.get(userRef)
            val friendSnapshot = transaction.get(friendRef)

            val userFriends = userSnapshot.get("friends") as? List<String> ?: emptyList()
            val friendFriends = friendSnapshot.get("friends") as? List<String> ?: emptyList()

            val updatedUserFriends = userFriends - friendId
            val updatedFriendFriends = friendFriends - userId

            transaction.update(userRef, "friends", updatedUserFriends)
            transaction.update(friendRef, "friends", updatedFriendFriends)
        }.await()
    }

    fun updateActivePackage(userId: String, packageId: String) {
        val userDoc = FirebaseFirestore.getInstance().collection("users").document(userId)
        userDoc.update("activePackageId", packageId)
    }

    suspend fun addToTotalPoints(userId: String, additionalPoints: Int) {
        val userDoc = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)

        FirebaseFirestore.getInstance().runTransaction { transaction ->
            val snapshot = transaction.get(userDoc)
            val currentPoints = snapshot.getLong("totalPoints") ?: 0L
            val newPoints = currentPoints + additionalPoints
            transaction.update(userDoc, "totalPoints", newPoints)
        }.await()
    }
}