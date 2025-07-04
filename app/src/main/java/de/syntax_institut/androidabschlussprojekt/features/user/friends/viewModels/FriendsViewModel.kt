package de.syntax_institut.androidabschlussprojekt.features.user.friends.viewModels

import android.app.Application
import android.content.Intent
import android.util.Log
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

    fun addFriendByUsername(
        myId: String,
        username: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                userRepository.getUserByUsername(username) { friendUser ->
                    val friendId = friendUser?.uid

                    Log.d("AddFriend", "myId: $myId, friendId: $friendId")

                    if (!friendId.isNullOrEmpty() && myId != friendId) {
                        viewModelScope.launch {
                            try {
                                userRepository.addFriendBothWays(myId, friendId)
                                onSuccess()
                            } catch (e: Exception) {
                                Log.e("AddFriend", "Fehler beim Speichern des Freundes", e)
                                onError()
                            }
                        }
                    } else {
                        Log.e("AddFriend", "Freund konnte nicht hinzugefügt werden")
                        onError()
                    }
                }
            } catch (e: Exception) {
                Log.e("AddFriend", "Fehler beim Laden des Freundes", e)
                onError()
            }
        }
    }

    fun startListeningToFriends(userId: String) {
        listenerRegistration?.remove()
        listenerRegistration = friendsRepository.listenToPickItFriends(userId) { friends ->
            _pickItFriends.value = friends
        }
    }


    fun removeFriend(friendId: String, onResult: () -> Unit = {}) {
        val userId = authViewModel.currentUserModel.value?.uid ?: return

        viewModelScope.launch {
            try {
                userRepository.removeFriendBothWays(userId, friendId)
                onResult()
            } catch (e: Exception) {
                Log.e("RemoveFriend", "Fehler beim Entfernen des Freundes: ${e.message}")
            }
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

    fun clearFriends() {
        listenerRegistration?.remove()
        _pickItFriends.value = emptyList()
    }
}
