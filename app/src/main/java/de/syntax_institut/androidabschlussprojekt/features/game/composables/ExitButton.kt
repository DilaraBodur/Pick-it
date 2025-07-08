package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ExitButton(onExit: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onExit, modifier = modifier) {
        Text(text = "Exit")
    }
}