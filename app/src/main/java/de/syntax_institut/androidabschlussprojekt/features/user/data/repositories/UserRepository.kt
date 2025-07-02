package de.syntax_institut.androidabschlussprojekt.features.user.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.SymbolPackage
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

    suspend fun getOwnedSymbolPackages(userId: String): List<SymbolPackage> {
        val snapshot = FirebaseFirestore.getInstance().collection("users").document(userId).get().await()
        val rawData = snapshot["ownedSymbolPackages"] ?: return emptyList()

        val type = Types.newParameterizedType(List::class.java, SymbolPackage::class.java)
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter<List<SymbolPackage>>(type)

        return adapter.fromJsonValue(rawData) ?: emptyList()
    }

    suspend fun saveOwnedSymbolPackages(userId: String, packages: List<SymbolPackage>) {
        val firestore = FirebaseFirestore.getInstance()
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, SymbolPackage::class.java)
        val adapter = moshi.adapter<List<SymbolPackage>>(type)

        val jsonCompatibleData = adapter.toJsonValue(packages)

        firestore.collection("users")
            .document(userId)
            .update("ownedSymbolPackages", jsonCompatibleData)
            .await()
    }
}