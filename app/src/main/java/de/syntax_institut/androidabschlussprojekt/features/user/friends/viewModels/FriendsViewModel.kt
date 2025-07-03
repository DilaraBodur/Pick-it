package de.syntax_institut.androidabschlussprojekt.features.user.friends.viewModels

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.user.data.repositories.UserRepository
import de.syntax_institut.androidabschlussprojekt.features.user.friends.data.models.UserFriend
import de.syntax_institut.androidabschlussprojekt.features.user.friends.data.repositories.FriendsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class FriendsViewModel(
    application: Application,
    private val friendsRepository: FriendsRepository,
    private val userRepository: UserRepository,
    private val authViewModel: AuthViewModel
) : AndroidViewModel(application) {

    private val _pickItFriends = MutableStateFlow<List<UserFriend>>(emptyList())
    val pickItFriends: StateFlow<List<UserFriend>> = _pickItFriends

    private var listenerRegistration: ListenerRegistration? = null

    init {
        startListeningToFriends()
    }

    fun addFriendByUsername(username: String, onSuccess: () -> Unit) {
        userRepository.getUserByUsername(username) { friendUser ->
            val myId = authViewModel.currentUserModel.value?.uid ?: return@getUserByUsername
            val friendId = friendUser?.uid

            if (friendId != null && myId != friendId) {
                userRepository.addFriend(myId, friendId)
                userRepository.addFriend(friendId, myId)

                startListeningToFriends()

                onSuccess()
            }
        }
    }

    fun startListeningToFriends() {
        val userId = authViewModel.currentUserModel.value?.uid ?: return
        listenerRegistration = friendsRepository.listenToPickItFriends(userId) { friends ->
            _pickItFriends.value = friends
        }
    }



    fun removeFriend(friendId: String) {
        val userId = authViewModel.currentUserModel.value?.uid ?: return
        viewModelScope.launch {
            friendsRepository.removePickItFriend(userId, friendId)
        }
    }

    fun inviteFriendToGame() {
        val inviteCode = generateInviteCode()
        val message = "Hey! Lust mitzuspielen? Starte Pick-It und gib diesen Code ein: $inviteCode"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val chooser = Intent.createChooser(sendIntent, "Freund einladen")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(chooser)
    }

    private fun generateInviteCode(): String {
        return UUID.randomUUID().toString().take(8).uppercase()
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}