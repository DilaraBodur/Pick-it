package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DiamondShape(
    size: Dp = 24.dp,
    color: Color = Color.White
) {
    Box(
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                rotationZ = 45f
            }
            .background(color, shape = RoundedCornerShape(4.dp))
    )
}