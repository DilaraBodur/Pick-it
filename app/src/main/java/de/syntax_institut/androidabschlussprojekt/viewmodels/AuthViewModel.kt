package de.syntax_institut.androidabschlussprojekt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import de.syntax_institut.androidabschlussprojekt.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authService: AuthService
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun loginAnonym() {
        viewModelScope.launch {
            authService.loginAnonym(
                onSuccess = { _isLoggedIn.value = true },
                onError = { println("Login-Fehler (anonym): ${it.message}") }
            )
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            authService.loginWithGoogle(
                idToken,
                onSuccess = { _isLoggedIn.value = true },
                onError = { println("Login-Fehler (Google): ${it.message}") }
            )
        }
    }

    fun loginWithFacebook(token: AccessToken) {
        viewModelScope.launch {
            authService.loginWithFacebook(
                token,
                onSuccess = { _isLoggedIn.value = true },
                onError = { println("Login-Fehler (Facebook): ${it.message}") }
            )
        }
    }

    fun logout() {
        authService.logout()
        _isLoggedIn.value = false
    }
}