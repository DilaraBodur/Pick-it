package de.syntax_institut.androidabschlussprojekt.features.game.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.SymbolPackage
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val userRepository: UserRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _ownedPackages = MutableStateFlow<List<SymbolPackage>>(emptyList())
    val ownedPackages: StateFlow<List<SymbolPackage>> = _ownedPackages

    init {
        viewModelScope.launch {
            authViewModel.currentUserModel.collect { user ->
                if (user != null) {
                    loadInventory()
                }
            }
        }
    }

    fun loadInventory() {
        viewModelScope.launch {
            val userId = authViewModel.currentUserModel.value?.uid ?: return@launch
            _ownedPackages.value = userRepository.getOwnedSymbolPackages(userId)
        }
    }
}