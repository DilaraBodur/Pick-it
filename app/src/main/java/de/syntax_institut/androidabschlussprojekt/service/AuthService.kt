package de.syntax_institut.androidabschlussprojekt.service

import com.google.firebase.auth.FirebaseAuth

class AuthService(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    fun loginAnonym(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        auth.signInAnonymously()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }
}