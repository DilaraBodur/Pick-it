package de.syntax_institut.androidabschlussprojekt.features.user.friends.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import de.syntax_institut.androidabschlussprojekt.features.user.friends.data.models.UserFriend
import de.syntax_institut.androidabschlussprojekt.features.user.data.models.User
import kotlinx.coroutines.tasks.await

class FriendsRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun listenToPickItFriends(userId: String, onFriendsChanged: (List<UserFriend>) -> Unit): ListenerRegistration {
        return FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, _ ->
                val user = snapshot?.toObject(User::class.java)
                val friendIds = user?.friends ?: emptyList()

                if (friendIds.isEmpty()) {
                    onFriendsChanged(emptyList())
                    return@addSnapshotListener
                }
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .whereIn("uid", friendIds)
                    .get()
                    .addOnSuccessListener { result ->
                        val friends = result.documents.mapNotNull { it.toObject(UserFriend::class.java) }
                        onFriendsChanged(friends)
                    }
            }
    }

    suspend fun removePickItFriend(userId: String, friendId: String) {
        val userRef = firestore.collection("users").document(userId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentFriends = snapshot.get("friends") as? List<String> ?: emptyList()
            val updatedFriends = currentFriends - friendId
            transaction.update(userRef, "friends", updatedFriends)
        }.await()
    }
}