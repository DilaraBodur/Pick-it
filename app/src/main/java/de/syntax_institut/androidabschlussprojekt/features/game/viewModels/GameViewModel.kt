package de.syntax_institut.androidabschlussprojekt.features.game.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
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

    private val _completedMissions = MutableStateFlow<Set<String>>(emptySet())
    val completedMissions: StateFlow<Set<String>> = _completedMissions

    private val _currentReels = MutableStateFlow<List<List<Symbol>>>(emptyList())
    val currentReels: StateFlow<List<List<Symbol>>> = _currentReels

    private val _currentPoints = MutableStateFlow(0)
    val currentPoints: StateFlow<Int> = _currentPoints

    private val _requiredPoints = MutableStateFlow(100)
    val requiredPoints: StateFlow<Int> = _requiredPoints

    private val _roundBonus = MutableStateFlow(0)
    val roundBonus: StateFlow<Int> = _roundBonus

    private val _bonusGivenForRound = MutableStateFlow(false)

    private val _timeProgress = MutableStateFlow(1f)
    val timeProgress: StateFlow<Float> = _timeProgress.asStateFlow()

    private val _bonusProgress = MutableStateFlow(0f)
    val bonusProgress: StateFlow<Float> =
        combine(_currentPoints, _requiredPoints) { current, required ->
            (current.toFloat() / required.toFloat()).coerceIn(0f, 1f)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, 0f)

    private val _isSpinning = MutableStateFlow(false)
    val isSpinning: StateFlow<Boolean> = _isSpinning

    private val _progress = mutableFloatStateOf(1f)
    val progress: State<Float> = _progress

    private var totalTimeMs: Long = 120_000L
    private var intervalMs: Long = 100L

    private val _heldSymbols = MutableStateFlow<Set<Int>>(emptySet())
    val heldSymbols: StateFlow<Set<Int>> = _heldSymbols

    private val _spinCount = MutableStateFlow(0)
    val spinCount: StateFlow<Int> = _spinCount

    private val _jokerUnlocked = MutableStateFlow(false)
    val jokerUnlocked: StateFlow<Boolean> = _jokerUnlocked

    private val _isSpinFinished = MutableStateFlow(false)
    val isSpinFinished = _isSpinFinished.asStateFlow()

    private val _showExitDialog = MutableStateFlow(false)
    val showExitDialog: StateFlow<Boolean> = _showExitDialog


    init {
        loadAllPackages()
    }

    fun openExitDialog() {
        _showExitDialog.value = true
    }

    fun closeExitDialog() {
        _showExitDialog.value = false
    }

    fun exitGame() {
        _showExitDialog.value = false
        resetGame()
    }

    fun toggleHold(index: Int) {
        val current = _heldSymbols.value.toMutableSet()
        if (current.contains(index)) {
            current.remove(index)
        } else {
            current.add(index)
        }
        _heldSymbols.value = current
    }

    fun startSpin(isAutoSpin: Boolean = false) {
        if (_spinCount.value >= 2 && !isAutoSpin) return

        if (!isAutoSpin) {
            _spinCount.value += 1
        }

        _isSpinning.value = true
        _isSpinFinished.value = false

        val resetMissions = _missionItems.value.map { mission ->
            mission.copy(isCompleted = false)
        }
        _missionItems.value = resetMissions

        viewModelScope.launch {
            if (isAutoSpin) {
                _isSpinning.value = true
            }
            delay(timeMillis = 2000)
            spinReels()

            _isSpinning.value = false

            val noMissionCompleted = _missionItems.value.none { it.isCompleted && !it.isClaimed }
            if (_spinCount.value >= 2 && noMissionCompleted) {
                val updatedMissions = _missionItems.value.map {
                    if (it.type == MissionType.JOKER) it.copy(isCompleted = true) else it
                }
                _missionItems.value = updatedMissions
                _jokerUnlocked.value = true
            }
        }
    }

    fun setSpinFinished() {
        _isSpinFinished.value = true
    }

    private fun resetSpinCountAndJoker() {
        _spinCount.value = 0
        _jokerUnlocked.value = false

        val resetMissions = _missionItems.value.map {
            if (it.type == MissionType.JOKER && !it.isClaimed) it.copy(isCompleted = false) else it
        }
        _missionItems.value = resetMissions
    }

    private fun spinReels() {
        val symbols = _selectedPackage.value?.symbols.orEmpty()
        if (symbols.isNotEmpty()) {
            val held = _heldSymbols.value

            val newReels = List(size = 5) { index ->
                if (held.contains(index)) {
                    _currentReels.value.getOrNull(index) ?: List(3) { symbols.random() }
                } else {
                    List(3) { symbols.random() }
                }
            }
            _currentReels.value = newReels
        }
       _isSpinFinished.value = true
    }

    fun updateRequiredPointsForRound(round: Int) {
        val calculatedPoints = 10000 + (round - 1) * 3000
        _requiredPoints.value = calculatedPoints
        _currentRound.value = round

        _bonusGivenForRound.value = false
    }

    private fun checkAndApplyBonus() {
        val current = _currentPoints.value
        val required = _requiredPoints.value

        if (current >= required) {
            val bonus = calculateBonusForRound(_currentRound.value)
            _roundBonus.value = bonus
            _totalPoints.value += bonus

            _bonusGivenForRound
        }
    }

    private fun calculateBonusForRound(round: Int): Int {
        return 1000 + (round - 1) * 500
    }

    private fun loadAllPackages() {
        viewModelScope.launch {
            _allPackages.value = symbolsRepository.loadSymbols()
            loadActivePackage()
        }
    }

    private fun loadActivePackage() {
        val activePackageId = authViewModel.currentUserModel.value?.activePackageId ?: "Standard"
        _selectedPackage.value = _allPackages.value.find { it.packageId == activePackageId }

        val symbols = _selectedPackage.value?.symbols.orEmpty()

        val generatedMissions = mutableListOf<MissionItem>()

        val symbolsToUse = symbols.take(6)

        symbolsToUse.forEachIndexed { index, symbol ->
            generatedMissions.add(
                MissionItem(
                    id = "three_$index",
                    type = MissionType.THREE,
                    symbol = symbol,
                    isCompleted = false,
                    basePoints = 0,
                    isClaimed = false
                )
            )
        }

        repeat(times = 2) { index ->
            generatedMissions.add(
                MissionItem(
                    id = "joker_$index",
                    type = MissionType.JOKER,
                    symbol = symbols.firstOrNull(),
                    isCompleted = false,
                    basePoints = 0,
                    isClaimed = false
                )
            )
        }

        generatedMissions.add(
            MissionItem(
                id = "fullhouse",
                type = MissionType.FULLHOUSE,
                symbol = symbols.firstOrNull(),
                isCompleted = false,
                basePoints = 0,
                isClaimed = false
            )
        )

        generatedMissions.add(
            MissionItem(
                id = "five_diff",
                type = MissionType.FIVE_DIFF,
                symbol = symbols.firstOrNull(),
                isCompleted = false,
                basePoints = 0,
                isClaimed = false
            )
        )

        generatedMissions.add(
            MissionItem(
                id = "four",
                type = MissionType.FOUR,
                symbol = symbols.firstOrNull(),
                isCompleted = false,
                basePoints = 0,
                isClaimed = false
            )
        )

        generatedMissions.add(
            MissionItem(
                id = "five",
                type = MissionType.FIVE,
                symbol = symbols.firstOrNull(),
                isCompleted = false,
                basePoints = 0,
                isClaimed = false
            )
        )

        _missionItems.value = generatedMissions

        spinReels()
    }


    private fun updatePoints(newPoints: Int) {
        _currentPoints.value = newPoints
    }

    fun updateRequiredPoints(newRequired: Int) {
        _requiredPoints.value = newRequired
    }

    private fun increaseBonusProgress(points: Float) {
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

        if (!_isSpinFinished.value) return
        val currentSymbols = _currentReels.value.mapNotNull { it.getOrNull(1) }
        if (currentSymbols.isEmpty()) return

        val symbolCounts = currentSymbols.groupingBy { it.id }.eachCount()
        val distinctCount = symbolCounts.size
        val maxCount = symbolCounts.values.maxOrNull() ?: 0

        val updatedMissions = _missionItems.value.map { mission ->

            when (mission.type) {

                MissionType.FIVE -> {
                    val count = symbolCounts[mission.symbol?.id] ?: symbolCounts.values.maxOrNull() ?: 0
                    if (count >= 5 && !mission.isCompleted) {
                        mission.copy(
                            isCompleted = true,
                            symbol = mission.symbol ?: currentSymbols.first(),
                            combinationType = "5er",
                            round = _currentRound.value
                        )
                    } else mission
                }

                MissionType.FOUR -> {
                    val count = symbolCounts[mission.symbol?.id] ?: symbolCounts.values.maxOrNull() ?: 0
                    if (count >= 4 && !mission.isCompleted) {
                        val combination = if (count >= 5) "5er" else "4er"
                        mission.copy(
                            isCompleted = true,
                            symbol = mission.symbol ?: currentSymbols.first(),
                            combinationType = combination,
                            round = _currentRound.value
                        )
                    } else mission
                }

                MissionType.THREE -> {
                    val count = symbolCounts[mission.symbol?.id] ?: 0
                    if (count >= 3 && !mission.isCompleted) {
                        val combination = when {
                            count >= 5 -> "5er"
                            count >= 4 -> "4er"
                            else -> "3er"
                        }
                        mission.copy(
                            isCompleted = true,
                            symbol = mission.symbol,
                            combinationType = combination,
                            round = _currentRound.value
                        )
                    } else mission
                }

                MissionType.FULLHOUSE -> {
                    if (symbolCounts.values.contains(3) && symbolCounts.values.contains(2) && !mission.isCompleted) {
                        mission.copy(
                            isCompleted = true,
                            symbol = mission.symbol ?: currentSymbols.first(),
                            combinationType = "fullhouse",
                            round = _currentRound.value
                        )
                    } else mission
                }

                MissionType.FIVE_DIFF -> {
                    if (distinctCount >= 5 && !mission.isCompleted) {
                        mission.copy(
                            isCompleted = true,
                            symbol = mission.symbol ?: currentSymbols.first(),
                            combinationType = "5verschiedene",
                            round = _currentRound.value
                        )
                    } else mission
                }

                MissionType.JOKER -> {
                    if (!mission.isCompleted) {
                        val (jokerPoints, combinationType) = when {
                            maxCount >= 5 -> calculatePointsUseCase.calculatePoints(
                                symbol = currentSymbols.firstOrNull() ?: mission.symbol!!,
                                combinationType = "5er",
                                round = _currentRound.value
                            ) to "5er"

                            maxCount >= 4 -> calculatePointsUseCase.calculatePoints(
                                symbol = currentSymbols.firstOrNull() ?: mission.symbol!!,
                                combinationType = "4er",
                                round = _currentRound.value
                            ) to "4er"

                            maxCount >= 3 -> {
                                calculatePointsUseCase.calculatePoints(
                                    symbol = currentSymbols.firstOrNull() ?: mission.symbol!!,
                                    combinationType = "3er",
                                    round = _currentRound.value
                                ) to "3er"
                            }

                            else -> calculatePointsUseCase.calculateJokerPoints(_currentRound.value) to "Joker"
                        }

                        mission.copy(
                            isCompleted = true,
                            basePoints = jokerPoints,
                            symbol = mission.symbol ?: currentSymbols.firstOrNull(),
                            combinationType = combinationType,
                            round = _currentRound.value
                        )
                    } else mission
                }
            }
        }

        _missionItems.value = updatedMissions
    }

    fun claimMission(mission: MissionItem) {
        val currentSymbols = _currentReels.value.mapNotNull { it.getOrNull(index = 1) }
        if (currentSymbols.isEmpty()) return

        val symbolCounts = currentSymbols.groupingBy { it.id }.eachCount()
        val maxCount = symbolCounts.values.maxOrNull() ?: 0
        val distinctCount = symbolCounts.size

        val (calculatedPoints, combinationType) = when (mission.type) {

            MissionType.JOKER -> {
                when {
                    maxCount >= 5 -> calculatePointsUseCase.calculatePoints(
                        symbol = currentSymbols.first(),
                        combinationType = "5er",
                        round = _currentRound.value
                    ) to "5er"

                    maxCount >= 4 -> calculatePointsUseCase.calculatePoints(
                        symbol = currentSymbols.first(),
                        combinationType = "4er",
                        round = _currentRound.value
                    ) to "4er"

                    maxCount >= 3 -> calculatePointsUseCase.calculatePoints(
                        symbol = currentSymbols.first(),
                        combinationType = "3er",
                        round = _currentRound.value
                    ) to "3er"

                    else -> calculatePointsUseCase.calculateJokerPoints(_currentRound.value) to "Joker"
                }
            }

            MissionType.THREE -> {
                when {
                    maxCount >= 5 -> calculatePointsUseCase.calculatePoints(currentSymbols.first(), "5er", _currentRound.value) to "5er"
                    maxCount >= 4 -> calculatePointsUseCase.calculatePoints(currentSymbols.first(), "4er", _currentRound.value) to "4er"
                    maxCount >= 3 -> calculatePointsUseCase.calculatePoints(currentSymbols.first(), "3er", _currentRound.value) to "3er"
                    else -> calculatePointsUseCase.calculateJokerPoints(_currentRound.value) to "Joker"
                }
            }

            MissionType.FOUR -> {
                when {
                    maxCount >= 5 -> calculatePointsUseCase.calculatePoints(currentSymbols.first(), "5er", _currentRound.value) to "5er"
                    maxCount >= 4 -> calculatePointsUseCase.calculatePoints(currentSymbols.first(), "4er", _currentRound.value) to "4er"
                    else -> calculatePointsUseCase.calculateJokerPoints(_currentRound.value) to "Joker"
                }
            }

            MissionType.FIVE -> {
                if (maxCount >= 5) {
                    calculatePointsUseCase.calculatePoints(currentSymbols.first(), "5er", _currentRound.value) to "5er"
                } else {
                    calculatePointsUseCase.calculateJokerPoints(_currentRound.value) to "Joker"
                }
            }

            MissionType.FULLHOUSE -> {
                if (symbolCounts.values.contains(3) && symbolCounts.values.contains(2)) {
                    calculatePointsUseCase.calculatePoints(currentSymbols.first(), "fullhouse", _currentRound.value) to "fullhouse"
                } else {
                    calculatePointsUseCase.calculateJokerPoints(_currentRound.value) to "Joker"
                }
            }

            MissionType.FIVE_DIFF -> {
                if (distinctCount >= 5) {
                    calculatePointsUseCase.calculatePoints(currentSymbols.first(), "5verschiedene", _currentRound.value) to "5verschiedene"
                } else {
                    calculatePointsUseCase.calculateJokerPoints(_currentRound.value) to "Joker"
                }
            }
        }

        val updatedMissions = _missionItems.value.map { item ->
            when {
                item.id == mission.id -> item.copy(
                    isClaimed = true,
                    basePoints = calculatedPoints,
                    combinationType = combinationType
                )

                item.isCompleted && item.isClaimed -> item.copy(isCompleted = false)

                else -> item
            }
        }

        _missionItems.value = updatedMissions

        increaseBonusProgress(calculatedPoints.toFloat())
        updatePoints(newPoints = _currentPoints.value + calculatedPoints)
        checkAndApplyBonus()

        _heldSymbols.value = emptySet()
        resetSpinCountAndJoker()
        startSpin(isAutoSpin = true)
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


    fun startTimer() {
        viewModelScope.launch {
            val steps = totalTimeMs / intervalMs
            for (i in steps downTo 0) {
                _progress.value = i / steps.toFloat()
                delay(intervalMs)
            }
            _progress.value = 0f
        }
    }

    fun resetTimer() {
        _progress.value = 1f
    }

}



