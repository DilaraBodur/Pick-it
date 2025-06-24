package de.syntax_institut.androidabschlussprojekt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.syntax_institut.androidabschlussprojekt.data.remote.toUserModel
import de.syntax_institut.androidabschlussprojekt.data.repository.UserRepository
import de.syntax_institut.androidabschlussprojekt.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AuthViewModel(
    private val authService: AuthService,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _isChecking = MutableStateFlow(true)
    val isChecking: StateFlow<Boolean> = _isChecking

    init {
        checkIfLoggedIn()
    }

    private fun checkIfLoggedIn() {
        _currentUser.value = FirebaseAuth.getInstance().currentUser
        _isChecking.value = false
    }

    fun loginAnonym() {
        viewModelScope.launch {
            try {
                val user = authService.loginAnonym()
                userRepository.saveUser(user.toUserModel())
                _currentUser.value = user
            } catch (e: Exception) {
                println("Fehler beim anonymen Login: ${e.message}")
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                val user = authService.loginWithGoogle(idToken)
                userRepository.saveUser(user.toUserModel())
                _currentUser.value = user
            } catch (e: Exception) {
                println("Google Login Fehler: ${e.message}")
            }
        }
    }

    fun loginWithFacebook(token: AccessToken) {
        viewModelScope.launch {
            try {
                val user = authService.loginWithFacebook(token)
                userRepository.saveUser(user.toUserModel())
                _currentUser.value = user
            } catch (e: Exception) {
                println("Facebook Login Fehler: ${e.message}")
            }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _currentUser.value = null
    }

    fun deleteAccount() {
        viewModelScope.launch {
            val user = FirebaseAuth.getInstance().currentUser ?: return@launch
            try {
                userRepository.deleteUser(user.uid)
                user.delete().addOnSuccessListener {
                    logout()
                }
            } catch (e: Exception) {
                println("Fehler beim Löschen: ${e.message}")
            }
        }
    }
}