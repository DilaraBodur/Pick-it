package de.syntax_institut.androidabschlussprojekt.features.user.friends.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.syntax_institut.androidabschlussprojekt.features.user.friends.data.models.UserFriend
import de.syntax_institut.androidabschlussprojekt.features.user.friends.viewModels.FriendsViewModel

@Composable
fun FriendListItem(
    friend: UserFriend,
    friendsViewModel: FriendsViewModel,
    onRemove: () -> Unit
) {

    Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = friend.photoUrl,
                contentDescription = null,
                modifier = Modifier.size(40.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(friend.username, fontWeight = FontWeight.Bold)
                Text("${friend.totalPoints} Punkte", style = MaterialTheme.typography.bodySmall)
            }
        }
        Row {
            IconButton(onClick = {
                friendsViewModel.inviteFriendToGame()
            }) {
                Icon(Icons.Default.Share, contentDescription = "Einladen")
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Entfernen")
            }
        }
    }
}