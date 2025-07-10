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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    private val _currentReels = MutableStateFlow<List<List<Symbol>>>(emptyList())
    val currentReels: StateFlow<List<List<Symbol>>> = _currentReels

    private val _currentPoints = MutableStateFlow(0)
    val currentPoints: StateFlow<Int> = _currentPoints

    private val _requiredPoints = MutableStateFlow(10000)
    val requiredPoints: StateFlow<Int> = _requiredPoints

    private val _timeProgress = MutableStateFlow(1f)
    val timeProgress: StateFlow<Float> = _timeProgress.asStateFlow()

    private val _bonusProgress = MutableStateFlow(0f)
    val bonusProgress: StateFlow<Float> =
        combine(_currentPoints, _requiredPoints) { current, required ->
            (current.toFloat() / required.toFloat()).coerceIn(0f, 1f)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, 0f)

    private val _isSpinning = MutableStateFlow(false)
    val isSpinning: StateFlow<Boolean> = _isSpinning

    init {
        loadAllPackages()
    }

    fun startSpin() {
        _isSpinning.value = true
        viewModelScope.launch {
            delay(2000)
            spinReels()
            _isSpinning.value = false
        }
    }

    fun spinReels() {
        val symbols = _selectedPackage.value?.symbols.orEmpty()
        if (symbols.isNotEmpty()) {
            val newReels = List(5) {
                List(3) { symbols.random() }
            }
            _currentReels.value = newReels
        }
    }

    private fun loadAllPackages() {
        viewModelScope.launch {
            _allPackages.value = symbolsRepository.loadSymbols()
            loadActivePackage()
        }
    }

    fun loadActivePackage() {
        val activePackageId = authViewModel.currentUserModel.value?.activePackageId ?: "Standard"
        _selectedPackage.value = _allPackages.value.find { it.packageId == activePackageId }

        val symbols = _selectedPackage.value?.symbols.orEmpty()

        val generatedMissions = mutableListOf<MissionItem>()

        symbols.take(6).forEachIndexed { index, symbol ->
            generatedMissions.add(
                MissionItem(
                    id = "three_$index",
                    type = MissionType.THREE,
                    symbol = symbol,
                    isCompleted = false
                )
            )
        }

        repeat(2) { index ->
            generatedMissions.add(
                MissionItem(
                    id = "joker_$index",
                    type = MissionType.JOKER,
                    symbol = symbols.getOrNull(index) ?: symbols.firstOrNull() ?: Symbol(
                        id = 0,
                        emoji = "❓",
                        name = "?",
                        basePoints = 0
                    ),
                    isCompleted = false
                )
            )
        }

        generatedMissions.add(
            MissionItem(
                id = "fullhouse",
                type = MissionType.FULLHOUSE,
                symbol = symbols.firstOrNull() ?: Symbol(
                    id = 0,
                    emoji = "❓",
                    name = "?",
                    basePoints = 0
                ),
                isCompleted = false
            )
        )

        generatedMissions.add(
            MissionItem(
                id = "five_diff",
                type = MissionType.FIVE_DIFF,
                symbol = symbols.firstOrNull() ?: Symbol(
                    id = 0,
                    emoji = "❓",
                    name = "?",
                    basePoints = 0
                ),
                isCompleted = false
            )
        )

        generatedMissions.add(
            MissionItem(
                id = "four",
                type = MissionType.FOUR,
                symbol = symbols.firstOrNull() ?: Symbol(
                    id = 0,
                    emoji = "❓",
                    name = "?",
                    basePoints = 0
                ),
                isCompleted = false
            )
        )

        generatedMissions.add(
            MissionItem(
                id = "five",
                type = MissionType.FIVE,
                symbol = symbols.firstOrNull() ?: Symbol(
                    id = 0,
                    emoji = "❓",
                    name = "?",
                    basePoints = 0
                ),
                isCompleted = false
            )
        )

        _missionItems.value = generatedMissions

        spinReels()
    }



    fun updatePoints(newPoints: Int) {
        _currentPoints.value = newPoints
    }

    fun updateRequiredPoints(newRequired: Int) {
        _requiredPoints.value = newRequired
    }

    fun increaseBonusProgress(points: Float) {
        val newProgress = (_bonusProgress.value + points).coerceIn(0f, 1f)
        _bonusProgress.value = newProgress
    }

    fun decreaseTimeProgress(step: Float) {
        val newProgress = (_timeProgress.value - step).coerceIn(0f, 1f)
        _timeProgress.value = newProgress
    }

    fun resetProgress() {
        _timeProgress.value = 1f
        _bonusProgress.value = 0f
    }

    fun evaluateCombination() {
        val currentSymbols = _currentReels.value.mapNotNull { reel ->
            reel.getOrNull(1)
        }

        if (currentSymbols.isEmpty()) return

        val symbolCounts = currentSymbols.groupingBy { it.id }.eachCount()
        val counts = symbolCounts.values.sortedDescending()

        val currentSymbol = currentSymbols.firstOrNull() ?: return
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

            highestCount == 3 -> calculatePointsUseCase.calculatePoints(
                symbol = currentSymbol,
                combinationType = "3er",
                round = _currentRound.value
            )

            else -> 0
        }

        _totalPoints.value = points
    }


    fun nextRound() {
        if (_currentRound.value < 5) {
            _currentRound.value += 1
            spinReels()
        } else {
            _gameFinished.value = true
        }
    }

    fun resetGame() {
        _currentRound.value = 1
        _totalPoints.value = 0
        spinReels()
    }
}



