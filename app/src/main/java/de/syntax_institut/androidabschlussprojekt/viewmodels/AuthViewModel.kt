package de.syntax_institut.androidabschlussprojekt.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.syntax_institut.androidabschlussprojekt.data.model.User
import de.syntax_institut.androidabschlussprojekt.data.remote.toUserModel
import de.syntax_institut.androidabschlussprojekt.data.repository.UserRepository
import de.syntax_institut.androidabschlussprojekt.data.repository.UsernameRepository
import de.syntax_institut.androidabschlussprojekt.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AuthViewModel(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val usernameRepository: UsernameRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _isChecking = MutableStateFlow(true)
    val isChecking: StateFlow<Boolean> = _isChecking

    private val _currentUserModel = MutableStateFlow<User?>(null)
    val currentUserModel: StateFlow<User?> = _currentUserModel

    init {
        checkIfLoggedIn()
    }

    private fun checkIfLoggedIn() {
        viewModelScope.launch {
            _isChecking.value = true
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            _currentUser.value = firebaseUser

            if (firebaseUser != null) {
                val userModel = userRepository.getUserById(firebaseUser.uid)
                _currentUserModel.value = userModel
            }
            _isChecking.value = false
        }
    }

    fun loginAnonym() {
        viewModelScope.launch {
            _isChecking.value = true
            try {
                val username = usernameRepository.getRandomUsername()
                val firebaseUser = authService.loginAnonym()
                val userModel = firebaseUser.toUserModel(username)

                userRepository.saveUser(userModel)
                _currentUser.value = firebaseUser
                _currentUserModel.value = userModel
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Anonymer Login fehlgeschlagen: ${e.message}")
            } finally {
                _isChecking.value = false
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isChecking.value = true
            try {
                val firebaseUser = authService.loginWithGoogle(idToken)
                val username = firebaseUser.displayName ?: "Player${firebaseUser.uid.take(5)}"
                val userModel = firebaseUser.toUserModel(username)

                userRepository.saveUser(userModel)
                _currentUser.value = firebaseUser
                _currentUserModel.value = userModel
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Google Login fehlgeschlagen: ${e.message}")
            } finally {
                _isChecking.value = false
            }
        }
    }

    fun loginWithFacebook(token: AccessToken) {
        viewModelScope.launch {
            _isChecking.value = true
            try {
                val firebaseUser = authService.loginWithFacebook(token)
                val username = firebaseUser.displayName ?: "Player${firebaseUser.uid.take(5)}"
                val userModel = firebaseUser.toUserModel(username)

                userRepository.saveUser(userModel)
                _currentUser.value = firebaseUser
                _currentUserModel.value = userModel
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Facebook Login fehlgeschlagen: ${e.message}")
            } finally {
                _isChecking.value = false
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