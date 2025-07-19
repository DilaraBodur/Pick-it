package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionItem
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionType

@Composable
fun MissionItemBox(
    mission: MissionItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(12.dp)
    val backgroundColor = Color(0xFF1565C0)
    val metallicSilver = Color(0xFFBFC1C2)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = mission.isCompleted && !mission.isClaimed) { onClick() }
            .border(width = 1.dp, color = if (mission.isCompleted && !mission.isClaimed) metallicSilver else Color.Black, shape = shape)
            .shadow(
                elevation = if (mission.isCompleted) 8.dp else 0.dp,
                shape = shape,
                clip = false
            )
            .background(backgroundColor, shape)
    ) {
        when {
            mission.isClaimed -> {
                Text(
                    text = "${mission.basePoints} Punkte",
                    color = Color.Green,
                    fontWeight = FontWeight.Bold
                )
            }
            mission.type == MissionType.THREE -> {
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(3) {
                        Text(
                            text = mission.symbol?.emoji ?: "❓",
                            fontSize = 22.sp,
                            modifier = Modifier.alpha(if (mission.isCompleted) 1f else 0.5f),
                            color = Color.White
                        )
                    }
                }
            }
            mission.type == MissionType.JOKER -> Text("🃏", color = Color.White, fontSize = 24.sp)
            mission.type == MissionType.FULLHOUSE -> Text("Fullhouse", color = Color.White, fontSize = 16.sp)
            mission.type == MissionType.FIVE_DIFF -> Text("5 verschiedene", color = Color.White, fontSize = 16.sp)
            mission.type == MissionType.FOUR -> Text("4er", color = Color.White, fontSize = 16.sp)
            mission.type == MissionType.FIVE -> Text("5er", color = Color.White, fontSize = 16.sp)
        }
    }
}