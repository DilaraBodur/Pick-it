package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BonusProgressBar(
    progress: Float,
    currentPoints: Int,
    requiredPoints: Int,
    rondBonus: Int,
    modifier: Modifier = Modifier
) {

    val shinyCyan = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFB2FFFF),
            Color(0xFF00E5FF),
            Color(0xFF0097A7),
            Color(0xFF00E5FF),
            Color(0xFFB2FFFF)
        )
    )

    Box(
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.7f),
                    topLeft = Offset(6f, 6f),
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(24f)
                )
            }
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .background(shinyCyan)
        )

        Text(
            text = "$currentPoints / $requiredPoints",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}