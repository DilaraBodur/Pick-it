package de.syntax_institut.androidabschlussprojekt.features.game.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.SymbolPackage
import de.syntax_institut.androidabschlussprojekt.features.game.data.repositories.SymbolsRepository
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val userRepository: UserRepository,
    private val symbolsRepository: SymbolsRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _ownedPackages = MutableStateFlow<List<SymbolPackage>>(emptyList())
    val ownedPackages: StateFlow<List<SymbolPackage>> = _ownedPackages

    val activePackageId: StateFlow<String> = authViewModel.currentUserModel
        .map { it?.activePackageId ?: "standard" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "standard")

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
            val allPackages = symbolsRepository.loadSymbols()
            val purchasedIds = userRepository.getPurchasedPackageIds(userId)
            _ownedPackages.value = allPackages.filter { it.packageId in purchasedIds }
        }
    }

    fun updateActivePackage(packageId: String) {
        val userId = authViewModel.currentUserModel.value?.uid ?: return
        viewModelScope.launch {
            userRepository.updateActivePackage(userId, packageId)
            authViewModel.refreshUser()
        }
    }
}