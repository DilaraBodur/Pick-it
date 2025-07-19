package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel

@Composable
fun DrawButtons(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    val spinCount by viewModel.spinCount.collectAsState()

    val silverBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF9E9E9E),
            Color(0xFFE0E0E0),
            Color(0xFFFFFFFF),
            Color(0xFFB0B0B0),
            Color(0xFF8A8A8A)
        ),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val shape = RoundedCornerShape(12.dp)

        Button(
            onClick = { viewModel.startSpin() },
            enabled = spinCount < 1,
            modifier = Modifier
                .drawBehind {
                    drawRoundRect(
                        color = Color.Black.copy(alpha = 0.7f),
                        topLeft = Offset(6f, 6f),
                        size = Size(size.width, size.height),
                        cornerRadius = CornerRadius(12.dp.toPx())
                    )
                }
                .clip(shape)
                .background(silverBrush, shape)
                .border(BorderStroke(1.dp, Color.Black), shape)
                .height(40.dp)
                .weight(1f)
                .fillMaxWidth(),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            )
        ) {
            Text("Draw")
        }

        Button(
            onClick = { viewModel.startSpin() },
            enabled = spinCount < 2,
            modifier = Modifier
                .drawBehind {
                    drawRoundRect(
                        color = Color.Black.copy(alpha = 0.7f),
                        topLeft = Offset(6f, 6f),
                        size = Size(size.width, size.height),
                        cornerRadius = CornerRadius(12.dp.toPx())
                    )
                }
                .clip(shape)
                .background(silverBrush, shape)
                .border(BorderStroke(1.dp, Color.Black), shape)
                .height(40.dp)
                .weight(1f)
                .fillMaxWidth(),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            )
        ) {
            Text("Draw")
        }
    }
}