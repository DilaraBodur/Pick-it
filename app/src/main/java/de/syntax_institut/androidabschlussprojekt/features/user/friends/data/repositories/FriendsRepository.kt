package de.syntax_institut.androidabschlussprojekt.features.user.friends.data.repositories


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import de.syntax_institut.androidabschlussprojekt.features.user.data.models.User
import de.syntax_institut.androidabschlussprojekt.features.user.friends.data.models.UserFriend
import kotlinx.coroutines.tasks.await

class FriendsRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    fun listenToPickItFriends(
        userId: String,
        onFriendsChanged: (List<UserFriend>) -> Unit
    ): ListenerRegistration {
        return usersCollection.document(userId)
            .addSnapshotListener { snapshot, _ ->
                val user = snapshot?.toObject(User::class.java)
                val friendIds = user?.friends ?: emptyList()

                if (friendIds.isEmpty()) {
                    onFriendsChanged(emptyList())
                    return@addSnapshotListener
                }

                usersCollection
                    .whereIn("uid", friendIds)
                    .get()
                    .addOnSuccessListener { result ->
                        val friends = result.documents.mapNotNull { it.toObject(UserFriend::class.java) }
                        onFriendsChanged(friends)
                    }
            }
    }

    suspend fun removePickItFriend(userId: String, friendId: String) {
        val userRef = usersCollection.document(userId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentFriends = snapshot.get("friends") as? List<String> ?: emptyList()
            val updatedFriends = currentFriends - friendId
            transaction.update(userRef, "friends", updatedFriends)
        }.await()
    }
}