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

    private val _progress = mutableFloatStateOf(1f)
    val progress: State<Float> = _progress

    private var totalTimeMs: Long = 120_000L
    private var intervalMs: Long = 100L

    private val _heldSymbols = MutableStateFlow<Set<Int>>(emptySet())
    val heldSymbols: StateFlow<Set<Int>> = _heldSymbols



    init {
        loadAllPackages()
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

    fun startSpin() {
        _isSpinning.value = true
        viewModelScope.launch {
            delay(2000)
            spinReels()
            evaluateCombination()
            _isSpinning.value = false
        }
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
        val currentSymbols = _currentReels.value.mapNotNull { it.getOrNull(1) }
        if (currentSymbols.isEmpty()) return

        val symbolCounts = currentSymbols.groupingBy { it.id }.eachCount()
        val distinctCount = symbolCounts.size
        val highestCount = symbolCounts.values.maxOrNull() ?: 0

        val updatedMissions = _missionItems.value.map { mission ->

            when (mission.type) {
                MissionType.THREE -> {
                    val count = symbolCounts[mission.symbol?.id] ?: 0
                    if (count >= 3 && !mission.isCompleted) {
                        mission.copy(
                            isCompleted = true,
                            basePoints = calculatePointsUseCase.calculatePoints(
                                symbol = mission.symbol!!,
                                combinationType = "3er",
                                round = _currentRound.value
                            )
                        )
                    } else mission
                }

                MissionType.FOUR -> {
                    val maxCount = symbolCounts.values.maxOrNull() ?: 0
                    if (maxCount >= 4 && !mission.isCompleted) {
                        mission.copy(
                            isCompleted = true,
                            basePoints = calculatePointsUseCase.calculatePoints(
                                symbol = mission.symbol ?: currentSymbols.first(),
                                combinationType = "4er",
                                round = _currentRound.value
                            )
                        )
                    } else mission
                }

                MissionType.FIVE -> {
                    val maxCount = symbolCounts.values.maxOrNull() ?: 0
                    if (maxCount >= 5 && !mission.isCompleted) {
                        mission.copy(
                            isCompleted = true,
                            basePoints = calculatePointsUseCase.calculatePoints(
                                symbol = mission.symbol ?: currentSymbols.first(),
                                combinationType = "5er",
                                round = _currentRound.value
                            )
                        )
                    } else mission
                }

                MissionType.FULLHOUSE -> {
                    if (symbolCounts.values.contains(3) && symbolCounts.values.contains(2) && !mission.isCompleted) {
                        mission.copy(
                            isCompleted = true,
                            basePoints = calculatePointsUseCase.calculatePoints(
                                symbol = mission.symbol ?: currentSymbols.first(),
                                combinationType = "fullhouse",
                                round = _currentRound.value
                            )
                        )
                    } else mission
                }

                MissionType.FIVE_DIFF -> {
                    if (distinctCount >= 5 && !mission.isCompleted) {
                        mission.copy(
                            isCompleted = true,
                            basePoints = calculatePointsUseCase.calculatePoints(
                                symbol = mission.symbol ?: currentSymbols.first(),
                                combinationType = "5verschiedene",
                                round = _currentRound.value
                            )
                        )
                    } else mission
                }

                MissionType.JOKER -> mission // Joker später extra behandeln (Wenn beide Drehungen gespielt wurden und keines der Missione passt)
            }
        }

        _missionItems.value = updatedMissions
    }

    fun claimMission(mission: MissionItem) {
        val updatedMissions = _missionItems.value.map {
            when {
                it.id == mission.id -> it.copy(isClaimed = true)
                it.isCompleted && !it.isClaimed -> it.copy(isCompleted = false)
                else -> it
            }
        }

        _missionItems.value = updatedMissions

        increaseBonusProgress(mission.basePoints.toFloat())
        updatePoints(_currentPoints.value + mission.basePoints)

        _heldSymbols.value = emptySet()
        startSpin()
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



