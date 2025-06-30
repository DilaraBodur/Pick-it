package de.syntax_institut.androidabschlussprojekt.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import de.syntax_institut.androidabschlussprojekt.data.model.User
import de.syntax_institut.androidabschlussprojekt.ui.components.AvatarImage
import de.syntax_institut.androidabschlussprojekt.ui.components.ProfileStatRow
import de.syntax_institut.androidabschlussprojekt.utils.countryCodeToEmoji

@Composable
fun ProfileDialog(
    user: User,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AvatarImage(
                        url = user.photoUrl ?: "",
                        onClick = onEditClick
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${countryCodeToEmoji(user.countryCode)} ${user.username}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Level ${user.level}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                TextButton(
                    onClick = onEditClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                    Text(
                        text = "Profil bearbeiten",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF1A237E),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Punkte: ${user.totalPoints}")
                LinearProgressIndicator(
                    progress = { (user.totalPoints % 1000) / 1000f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(8.dp),
                    color = Color.Green,
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                val stats = user.stats
                Column(modifier = Modifier.fillMaxWidth()) {
                    ProfileStatRow("Siegquote", "${stats.winRate}%")
                    ProfileStatRow("Aktuelle Siegesserie", "${stats.currentStreak}")
                    ProfileStatRow("Max. Siegesserie", "${stats.maxStreak}")
                    ProfileStatRow("2 vs 2-Siege", "${stats.twoVsTwoWins}")
                    ProfileStatRow("3-Spieler-Siege", "${stats.threePlayerWins}")
                    ProfileStatRow("4-Spieler-Siege", "${stats.fourPlayerWins}")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Schließen")
                }
            }
        }
    }
}