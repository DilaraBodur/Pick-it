package de.syntax_institut.androidabschlussprojekt.features.game.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.SymbolPackage
import de.syntax_institut.androidabschlussprojekt.features.game.data.repositories.SymbolsRepository
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopViewModel(
    private val symbolsRepository: SymbolsRepository,
    private val userRepository: UserRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    var symbolPackages by mutableStateOf<List<SymbolPackage>>(emptyList())
        private set

    var isGuest by mutableStateOf(false)
        private set

    var boughtPackageTitles by mutableStateOf<List<String>>(emptyList())
        private set


    val activePackageId: StateFlow<String> = authViewModel.currentUserModel
        .map { it?.activePackageId ?: "standard" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "standard")


    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            isGuest = authViewModel.currentUserModel.value?.loginProvider == "anonymous"
            symbolPackages = symbolsRepository.loadSymbols()

            authViewModel.currentUserModel.value?.uid?.let { uid ->
                boughtPackageTitles = userRepository.getPurchasedPackageIds(uid)
            }
        }
    }

    fun buyPackage(pkg: SymbolPackage) {
        viewModelScope.launch {
            val user = authViewModel.currentUserModel.value ?: return@launch
            val uid = user.uid

            if (boughtPackageTitles.contains(pkg.packageId)) return@launch

            userRepository.addPurchasedPackageId(uid, pkg.packageId)
            boughtPackageTitles = boughtPackageTitles + pkg.packageId
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