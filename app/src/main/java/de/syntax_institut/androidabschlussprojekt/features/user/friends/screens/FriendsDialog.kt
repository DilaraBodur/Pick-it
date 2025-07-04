package de.syntax_institut.androidabschlussprojekt.features.user.friends.screens


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.user.friends.screens.components.FriendListItem
import de.syntax_institut.androidabschlussprojekt.features.user.friends.viewModels.FriendsViewModel

@Composable
fun FriendsDialog(
    authViewModel: AuthViewModel,
    friendsViewModel: FriendsViewModel,
    onDismiss: () -> Unit
) {
    val currentUser by authViewModel.currentUserModel.collectAsState()
    val pickItFriends by friendsViewModel.pickItFriends.collectAsState()
    val isFacebookLogin = currentUser?.loginProvider == "facebook"

    var selectedTab by remember { mutableStateOf(if (isFacebookLogin) "Facebook" else "PickIt") }
    var showAddFriendDialog by remember { mutableStateOf(false) }


    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Freunde", style = MaterialTheme.typography.titleLarge)

                Spacer(Modifier.height(8.dp))

                if (isFacebookLogin) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        TextButton(onClick = { selectedTab = "Facebook" }) {
                            Text("Facebook")
                        }
                        TextButton(onClick = { selectedTab = "PickIt" }) {
                            Text("Pick-It")
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (selectedTab == "PickIt" || !isFacebookLogin) {
                    if (pickItFriends.isEmpty()) {
                        Text("Du hast noch keine Pick-It Freunde.")
                    } else {
                        LazyColumn {
                            items(pickItFriends) { friend ->
                                FriendListItem(
                                    friend = friend,
                                    friendsViewModel = friendsViewModel,
                                    onRemove = { friendsViewModel.removeFriend(friend.uid) }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { showAddFriendDialog = true },
                        enabled = currentUser != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Freund hinzufügen")
                    }
                } else {
                    Text("Facebook-Freunde anzeigen (kommt)")
                }

                Spacer(Modifier.height(16.dp))

                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Schließen")
                }
            }
        }
    }

    if (showAddFriendDialog) {
        AddFriendDialog(
            onConfirm = { username ->
                val myId = authViewModel.currentUserModel.value?.uid ?: return@AddFriendDialog

                friendsViewModel.addFriendByUsername(
                    myId = myId,
                    username = username,
                    onSuccess = {
                        Log.d("AddFriend", "Freund wurde hinzugefügt")
                        showAddFriendDialog = false
                    },
                    onError = {
                        Log.e("AddFriend", "Freund konnte nicht hinzugefügt werden")
                    }
                )
            },
            onDismiss = { showAddFriendDialog = false }
        )
    }
}


fun shareInvitation(context: Context, code: String) {
    val deepLink = "https://www.pick-it-game.com/invite/$code"
    val message = "Möchtest du mit mir spielen? Hier klicken: $deepLink"

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "Freund einladen")
    context.startActivity(shareIntent)
}