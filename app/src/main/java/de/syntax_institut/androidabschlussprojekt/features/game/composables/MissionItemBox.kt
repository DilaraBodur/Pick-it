package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionItem
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionType

@Composable
fun MissionItemBox(
    mission: MissionItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(6.dp)
    val backgroundColor = Color(0xFF1565C0)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(enabled = mission.isCompleted && !mission.isClaimed) { onClick() }
            .border(width = 1.dp, color = if (mission.isCompleted && !mission.isClaimed) Color.Yellow else Color.Black, shape = shape)
            .shadow(
                elevation = if (mission.isCompleted) 8.dp else 0.dp,
                shape = shape,
                clip = false
            )
            .background(backgroundColor, shape)
            .padding(vertical = 8.dp)
    ) {
        when {
            mission.isClaimed -> {
                Text(
                    text = "+${mission.basePoints} Punkte",
                    color = Color.Yellow,
                    fontWeight = FontWeight.Bold
                )
            }
            mission.type == MissionType.THREE -> {
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(3) {
                        Text(
                            text = mission.symbol?.emoji ?: "❓",
                            modifier = Modifier.alpha(if (mission.isCompleted) 1f else 0.3f),
                            color = Color.White
                        )
                    }
                }
            }
            mission.type == MissionType.JOKER -> Text("🃏", color = Color.White)
            mission.type == MissionType.FULLHOUSE -> Text("Fullhouse", color = Color.White)
            mission.type == MissionType.FIVE_DIFF -> Text("5 verschiedene", color = Color.White)
            mission.type == MissionType.FOUR -> Text("4er", color = Color.White)
            mission.type == MissionType.FIVE -> Text("5er", color = Color.White)
        }
    }
}