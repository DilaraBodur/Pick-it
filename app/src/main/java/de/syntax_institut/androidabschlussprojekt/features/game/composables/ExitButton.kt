package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = "Exit",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}