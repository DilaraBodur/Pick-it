package de.syntax_institut.androidabschlussprojekt.features.game.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import de.syntax_institut.androidabschlussprojekt.features.user.data.models.User
import de.syntax_institut.androidabschlussprojekt.utils.countryCodeToEmoji

@Composable
fun RankingDialog(
    topUsers: List<User>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Rangliste",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                topUsers.forEachIndexed { index, user ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = "${index + 1}.",
                            modifier = Modifier.width(24.dp),
                            fontWeight = FontWeight.Bold
                        )

                        AsyncImage(
                            model = user.photoUrl ?: "https://api.dicebear.com/7.x/adventurer/png?seed=placeholder",
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        val flagEmoji = countryCodeToEmoji(user.countryCode)
                        Text(text = flagEmoji, fontSize = 26.sp)

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(text = user.username, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                            Text(text = "${user.totalPoints} Punkte", fontSize = 16.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Schließen")
            }
        }
    )
}