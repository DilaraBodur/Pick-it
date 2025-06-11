package de.syntax_institut.androidabschlussprojekt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax_institut.androidabschlussprojekt.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authService: AuthService = AuthService()
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun loginAnonym() {
        viewModelScope.launch {
            authService.loginAnonym(
                onSuccess = { _isLoggedIn.value = true },
                onError = { println("Login-Fehler: ${it.message}") }
            )
        }
    }
}