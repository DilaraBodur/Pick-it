package de.syntax_institut.androidabschlussprojekt.features.game.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.SymbolPackage
import de.syntax_institut.androidabschlussprojekt.features.game.data.repositories.SymbolsRepository
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UserRepository
import kotlinx.coroutines.launch

class ShopViewModel(
    private val symbolsRepository: SymbolsRepository,
    private val userRepository: UserRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    var symbolPackages by mutableStateOf<List<SymbolPackage>>(emptyList())
        private set

    var isGuest by mutableStateOf(false)

    var boughtPackageTitles = mutableStateListOf<String>()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            isGuest = authViewModel.currentUserModel.value?.loginProvider == "anonymous"
            symbolPackages = symbolsRepository.loadSymbols()

            authViewModel.currentUserModel.value?.uid?.let { uid ->
                boughtPackageTitles.clear()
                boughtPackageTitles.addAll(userRepository.getOwnedSymbolPackages(uid).map { it.packageId })
            }
        }
    }

    fun buyPackage(pkg: SymbolPackage) {
        viewModelScope.launch {
            val user = authViewModel.currentUserModel.value ?: return@launch
            val currentInventory = userRepository.getOwnedSymbolPackages(user.uid)
            if (currentInventory.any { it.name == pkg.name }) return@launch

            val updatedInventory = currentInventory + pkg
            userRepository.saveOwnedSymbolPackages(user.uid, updatedInventory)

            boughtPackageTitles.add(pkg.packageId)
            loadData()
        }
    }
}