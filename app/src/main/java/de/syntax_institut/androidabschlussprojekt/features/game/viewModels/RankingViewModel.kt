package de.syntax_institut.androidabschlussprojekt.features.game.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax_institut.androidabschlussprojekt.features.user.data.models.User
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RankingViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _topUsers = MutableStateFlow<List<User>>(emptyList())
    val topUsers: StateFlow<List<User>> = _topUsers.asStateFlow()

    fun loadTopUsers() {
        viewModelScope.launch {
            _topUsers.value = userRepository.getTopUsersByPoints()
        }
    }
}