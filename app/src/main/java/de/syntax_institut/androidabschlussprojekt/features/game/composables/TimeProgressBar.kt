package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            Color.Cyan,
            Color.Blue,
            Color(0xFF8B00FF)
        )
    )

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(coercedProgress)
                .clip(RoundedCornerShape(8.dp))
                .background(rainbowBrush)
        )
    }
}