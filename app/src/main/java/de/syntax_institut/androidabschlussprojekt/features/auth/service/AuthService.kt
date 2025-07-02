package de.syntax_institut.androidabschlussprojekt.features.auth.service

import com.facebook.AccessToken
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class AuthService {
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun loginAnonym(): FirebaseUser {
        return try {
            val result = auth.signInAnonymously().await()
            result.user ?: throw Exception("Kein Benutzer erhalten")
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun loginWithGoogle(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val result = auth.signInWithCredential(credential).await()
            result.user ?: throw Exception("Kein Benutzer erhalten")
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun loginWithFacebook(token: AccessToken): FirebaseUser {
        val credential = FacebookAuthProvider.getCredential(token.token)
        return try {
            val result = auth.signInWithCredential(credential).await()
            result.user ?: throw Exception("Kein Benutzer erhalten")
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun linkWithGoogle(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val result = Firebase.auth.currentUser?.linkWithCredential(credential)?.await()
            result?.user ?: throw Exception("Google-Verknüpfung fehlgeschlagen")
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun linkWithFacebook(token: AccessToken): FirebaseUser {
        val credential = FacebookAuthProvider.getCredential(token.token)
        return try {
            val result = Firebase.auth.currentUser?.linkWithCredential(credential)?.await()
            result?.user ?: throw Exception("Facebook-Verknüpfung fehlgeschlagen")
        } catch (e: Exception) {
            throw e
        }
    }
}