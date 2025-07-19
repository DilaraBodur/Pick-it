package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun TimeProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val coercedProgress = progress.coerceIn(0f, 1f)

    val rainbowBrush = Brush.horizontalGradient(
        colors = listOf(
            Color.Red,
            Color(0xFFFFA500),
            Color.Yellow,
            Color.Green,
            Color.Cyan
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
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(rainbowBrush)
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(1f - coercedProgress)
                .align(Alignment.CenterEnd)
                .background(Color.LightGray)
        )
    }
}