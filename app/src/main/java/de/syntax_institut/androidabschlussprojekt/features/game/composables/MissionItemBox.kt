package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
    val darkGray = Color(0xFF2E2E2E)

    val blueCyanGlossyBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00BCD4),
            Color(0xFF00BCD4),
            Color(0xFF80DEEA),
            Color(0xFFE1F5FE),
            Color(0xFF80DEEA),
            Color(0xFF00BCD4),
            Color(0xFF00BCD4)
        ),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    val backgroundBrush = if (mission.isClaimed) {
        SolidColor(darkGray)
    } else if (mission.isCompleted) {
        blueCyanGlossyBrush
    } else {
        SolidColor(backgroundColor)
    }

    val textColor = if (mission.isClaimed) Color.White else Color.Black

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.7f),
                    topLeft = Offset(6f, 6f),
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(32f)
                )
            }
            .background(
                brush = backgroundBrush,
                shape = shape
            )
            .clip(shape)
            .clickable(enabled = mission.isCompleted && !mission.isClaimed) { onClick() }

    ) {
        when {
            mission.isClaimed -> {
                Text(
                    text = "${mission.basePoints} Punkte",
                    color = textColor
                )
            }
            mission.type == MissionType.THREE -> {
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(3) {
                        Text(
                            text = mission.symbol?.emoji ?: "❓",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }
                }
            }
            mission.type == MissionType.JOKER -> Text("🃏", color = Color.Black, fontSize = 24.sp)
            mission.type == MissionType.FULLHOUSE -> Text("Fullhouse", color = Color.Black, fontSize = 16.sp)
            mission.type == MissionType.FIVE_DIFF -> Text("5 verschiedene", color = Color.Black, fontSize = 16.sp)
            mission.type == MissionType.FOUR -> Text("4er", color = Color.Black, fontSize = 16.sp)
            mission.type == MissionType.FIVE -> Text("5er", color = Color.Black, fontSize = 16.sp)
        }
    }
}