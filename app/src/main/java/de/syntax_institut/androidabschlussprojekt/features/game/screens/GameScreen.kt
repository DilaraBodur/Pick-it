package de.syntax_institut.androidabschlussprojekt.features.game.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = koinViewModel()
) {
    val selectedPackage by viewModel.selectedPackage.collectAsState()
    val currentSymbols by viewModel.currentSymbols.collectAsState()
    val currentRound by viewModel.currentRound.collectAsState()

    val gameFinished by viewModel.gameFinished.collectAsState()

    val totalPoints by viewModel.totalPoints.collectAsState()
    val resultText = when {
        totalPoints >= 15000 -> "🎉 Du hast gewonnen!"
        else -> "😢 Leider verloren"
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Paket: ${selectedPackage?.name ?: "Standard"}", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            currentSymbols.forEach { symbol ->
                Card(Modifier.size(60.dp), elevation = CardDefaults.cardElevation(4.dp)) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(symbol.emoji, fontSize = 30.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Runde: $currentRound", fontSize = 18.sp)
        Text("Punkte: $totalPoints", fontSize = 18.sp)

        Spacer(Modifier.height(16.dp))

        Button(onClick = { viewModel.spinSymbols() }, Modifier.fillMaxWidth()) {
            Text("Spin")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { viewModel.evaluateCombination() }, Modifier.fillMaxWidth()) {
            Text("Kombi prüfen & Punkte")
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { viewModel.nextRound() }) { Text("Nächste Runde") }
            Button(onClick = { viewModel.resetGame() }) { Text("Reset") }
        }
    }

    if (gameFinished) {
        AlertDialog(
            onDismissRequest = {
                navController.navigate("lobby") {
                    popUpTo("lobby") { inclusive = false }
                }
            },
            title = { Text("Spiel beendet") },
            text = { Text("$resultText\nPunkte: $totalPoints") },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate("lobby") {
                            popUpTo("lobby") { inclusive = false }
                        }
                    }
                ) {
                    Text("Zurück zur Lobby")
                }
            }
        )
    }
}