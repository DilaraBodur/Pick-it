package de.syntax_institut.androidabschlussprojekt.features.game.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionItem
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionType
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.Symbol
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.SymbolPackage
import de.syntax_institut.androidabschlussprojekt.features.game.data.repositories.SymbolsRepository
import de.syntax_institut.androidabschlussprojekt.features.game.domain.usecases.CalculatePointsUseCase
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val symbolsRepository: SymbolsRepository,
    private val userRepository: UserRepository,
    private val calculatePointsUseCase: CalculatePointsUseCase,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _allPackages = MutableStateFlow<List<SymbolPackage>>(emptyList())
    val allPackages: StateFlow<List<SymbolPackage>> = _allPackages

    private val _selectedPackage = MutableStateFlow<SymbolPackage?>(null)
    val selectedPackage: StateFlow<SymbolPackage?> = _selectedPackage

    private val _currentRound = MutableStateFlow(1)
    val currentRound: StateFlow<Int> = _currentRound

    private val _totalPoints = MutableStateFlow(0)
    val totalPoints: StateFlow<Int> = _totalPoints

    private val _currentSymbols = MutableStateFlow<List<Symbol>>(emptyList())
    val currentSymbols: StateFlow<List<Symbol>> = _currentSymbols

    private val _gameFinished = MutableStateFlow(false)
    val gameFinished: StateFlow<Boolean> = _gameFinished

    private val _missionItems = MutableStateFlow<List<MissionItem>>(emptyList())
    val missionItems: StateFlow<List<MissionItem>> = _missionItems

    init {
        loadAllPackages()
    }

    private fun loadAllPackages() {
        viewModelScope.launch {
            _allPackages.value = symbolsRepository.loadSymbols()
            loadActivePackage()
        }
    }

    fun loadActivePackage() {
        val activePackageId = authViewModel.currentUserModel.value?.activePackageId ?: "standard"
        _selectedPackage.value = _allPackages.value.find { it.packageId == activePackageId }

        val symbols = _selectedPackage.value?.symbols.orEmpty()
        _missionItems.value = symbols.map { symbol ->
            MissionItem(
                id = symbol.id.toString(),
                type = MissionType.THREE,
                symbol = symbol,
                isCompleted = false
            )
        }

        spinSymbols()
    }

    fun spinSymbols() {
        val symbols = _selectedPackage.value?.symbols.orEmpty()
        if (symbols.isNotEmpty()) {
            _currentSymbols.value = List(5) { symbols.random() }
        }
    }

    fun evaluateCombination() {
        val symbolCounts = _currentSymbols.value.groupingBy { it.id }.eachCount()
        val counts = symbolCounts.values.sortedDescending()

        val currentSymbol = _currentSymbols.value.firstOrNull() ?: return
        val highestCount = counts.firstOrNull() ?: 0
        val distinctCount = symbolCounts.size

        val points = when {
            highestCount == 5 -> calculatePointsUseCase.calculatePoints(
                symbol = currentSymbol,
                combinationType = "5er",
                round = _currentRound.value
            )

            highestCount == 4 -> calculatePointsUseCase.calculatePoints(
                symbol = currentSymbol,
                combinationType = "4er",
                round = _currentRound.value
            )

            counts.contains(3) && counts.contains(2) -> calculatePointsUseCase.calculatePoints(
                symbol = currentSymbol,
                combinationType = "fullhouse",
                round = _currentRound.value
            )

            distinctCount == 5 -> calculatePointsUseCase.calculatePoints(
                symbol = currentSymbol,
                combinationType = "5verschiedene",
                round = _currentRound.value
            )

            highestCount == 3 -> {
                val symbolId = currentSymbol.id
                _missionItems.value = _missionItems.value.map { mission ->
                    if (mission.symbol?.id == symbolId) mission.copy(isCompleted = true) else mission
                }

                calculatePointsUseCase.calculatePoints(
                    symbol = currentSymbol,
                    combinationType = "3er",
                    round = _currentRound.value
                )
            }

            else -> 0
        }

        _totalPoints.value = points
    }

    fun nextRound() {
        if (_currentRound.value < 5) {
            _currentRound.value += 1
            spinSymbols()
        } else {
            _gameFinished.value = true
        }
    }

    fun resetGame() {
        _currentRound.value = 1
        _totalPoints.value = 0
        spinSymbols()
    }
}