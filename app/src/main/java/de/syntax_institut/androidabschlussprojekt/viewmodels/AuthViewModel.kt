package de.syntax_institut.androidabschlussprojekt.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.identity.util.UUID
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
                val avatarSeed = UUID.randomUUID().toString()
                val avatarUrl = "https://api.dicebear.com/7.x/adventurer/png?seed=$avatarSeed"
                val firebaseUser = authService.loginAnonym()
                val userModel = firebaseUser.toUserModel(
                    username, avatarUrl,
                    email = null,
                    photoUrl = null
                )

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

                val fullName = firebaseUser.displayName ?: "Player"
                val username = fullName.split(" ").firstOrNull() ?: fullName

                val firebasePhotoUrl = firebaseUser.photoUrl?.toString()
                val fallbackAvatarUrl = "https://api.dicebear.com/7.x/adventurer/png?seed=${UUID.randomUUID()}"

                val avatarUrl = if (isValidGooglePhotoUrl(firebasePhotoUrl)) {
                    firebasePhotoUrl!!
                } else {
                    fallbackAvatarUrl
                }

                val userModel = firebaseUser.toUserModel(
                    username, avatarUrl,
                    email = firebaseUser.email,
                    photoUrl = avatarUrl
                )
                userRepository.saveUser(userModel)
                _currentUser.value = firebaseUser
                _currentUserModel.value = userModel

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Fehler beim Google Login: ${e.message}")
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

                val fullName = firebaseUser.displayName ?: "Player"
                val username = fullName.split(" ").firstOrNull() ?: fullName

                val avatarUrl = firebaseUser.photoUrl?.toString()

                val userModel = avatarUrl?.let {
                    firebaseUser.toUserModel(
                        username, it,
                        email = firebaseUser.email,
                        photoUrl = avatarUrl
                    )
                }
                if (userModel != null) {
                    userRepository.saveUser(userModel)
                }
                _currentUser.value = firebaseUser
                _currentUserModel.value = userModel

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Fehler beim Facebook Login: ${e.message}")
            } finally {
                _isChecking.value = false
            }
        }
    }

    fun linkWithGoogle(
        idToken: String,
        displayName: String?,
        email: String?,
        photoUrl: String?
    ) {
        viewModelScope.launch {
            _isChecking.value = true
            try {
                val firebaseUser = authService.linkWithGoogle(idToken)

                val fullName = displayName ?: "Player"
                val username = fullName.split(" ").firstOrNull() ?: fullName
                val avatarUrl = if (!photoUrl.isNullOrEmpty()) photoUrl
                else "https://api.dicebear.com/7.x/adventurer/png?seed=${UUID.randomUUID()}"

                val userModel = firebaseUser.toUserModel(
                    username = username,
                    fullName = fullName,
                    email = email,
                    photoUrl = avatarUrl
                )

                userRepository.saveUser(userModel)
                _currentUser.value = firebaseUser
                _currentUserModel.value = userModel

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Fehler beim Google-Link: ${e.message}")
            } finally {
                _isChecking.value = false
            }
        }
    }

    fun linkWithFacebook(token: AccessToken) {
        viewModelScope.launch {
            _isChecking.value = true
            try {
                val firebaseUser = authService.linkWithFacebook(token)

                val fullName = firebaseUser.displayName ?: "Player"
                val username = fullName.split(" ").firstOrNull() ?: fullName

                val firebasePhotoUrl = firebaseUser.photoUrl?.toString()
                val fallbackAvatarUrl = "https://api.dicebear.com/7.x/adventurer/png?seed=${UUID.randomUUID()}"

                val avatarUrl = firebasePhotoUrl ?: fallbackAvatarUrl

                val userModel = firebaseUser.toUserModel(
                    username, avatarUrl,
                    email = firebaseUser.email,
                    photoUrl = avatarUrl
                )
                userRepository.saveUser(userModel)

                _currentUser.value = firebaseUser
                _currentUserModel.value = userModel

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Fehler beim Facebook-Link: ${e.message}")
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

    private fun isValidGooglePhotoUrl(url: String?): Boolean {
        return !url.isNullOrBlank() && url.contains("googleusercontent.com")
    }
}