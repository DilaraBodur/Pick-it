package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.Symbol

@Composable
fun SlotReel(reelSymbols: List<Symbol>) {
    Box(
        modifier = Modifier
            .width(90.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF222222))
            .clipToBounds()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-30).dp)
        ) {
            Text(
                text = reelSymbols[0].emoji,
                fontSize = 48.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = reelSymbols[1].emoji,
                fontSize = 56.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (30).dp)
        ) {
            Text(
                text = reelSymbols[2].emoji,
                fontSize = 48.sp,
                color = Color.White
            )
        }
    }
}