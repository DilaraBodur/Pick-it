package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionItem
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionType

@Composable
fun MissionItemBox(mission: MissionItem, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(6.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(
                elevation = if (mission.isCompleted) 8.dp else 0.dp,
                shape = shape,
                clip = false
            )
            .background(Color(0xFF8A2BE2), shape)
            .padding(vertical = 8.dp)
    ) {
        when (mission.type) {
            MissionType.THREE -> Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(3) {
                    Text(
                        text = mission.symbol?.emoji ?: "❓",
                        modifier = Modifier.alpha(if (mission.isCompleted) 1f else 0.3f),
                        color = Color.White
                    )
                }
            }
            MissionType.JOKER -> Text(text = "🃏", color = Color.White)
            MissionType.FULLHOUSE -> Text(text = "Fullhouse", color = Color.White)
            MissionType.FIVE_DIFF -> Text(text = "5 verschiedene", color = Color.White)
            MissionType.FOUR -> Text(text = "4er", color = Color.White)
            MissionType.FIVE -> Text(text = "5er", color = Color.White)
        }
    }
}