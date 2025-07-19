package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ExitButton(
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onExit,
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.7f),
                    topLeft = Offset(6f, 6f),
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(30f)
                )
            }
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = "Exit",
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}