package de.syntax_institut.androidabschlussprojekt.features.game.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.ShopViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = koinViewModel()
) {
    val packages = viewModel.symbolPackages
    val isGuest = viewModel.isGuest
    val boughtTitles = viewModel.boughtPackageTitles

    val activePackageId = viewModel.activePackageId.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Shop", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        if (isGuest) {
            Text(
                "Bitte melde dich mit deinem Google- oder Facebook-Konto an, um Symbolpakete zu kaufen.",
                color = Color.Red,
                fontWeight = FontWeight.SemiBold
            )
        }

        LazyColumn {
            items(packages) { symbolPackage ->
                val alreadyBought = boughtTitles.contains(symbolPackage.packageId)
                val isActive = symbolPackage.packageId == activePackageId.value

                val buttonText = when {
                    symbolPackage.packageId == "standard" && activePackageId.value == "standard" -> "Aktiv"
                    symbolPackage.packageId == "standard" && activePackageId.value != "standard" -> "Verwenden"

                    isActive -> "Aktiv"
                    alreadyBought -> "Verwenden"
                    else -> "Kaufen"
                }

                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(symbolPackage.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            symbolPackage.symbols.forEach {
                                Text(it.emoji, fontSize = 30.sp)
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                            when (buttonText) {
                                "Kaufen" -> Button(
                                    onClick = { viewModel.buyPackage(symbolPackage) },
                                    enabled = !isGuest
                                ) {
                                    Text("Kaufen")
                                }

                                "Verwenden" -> Button(
                                    onClick = { viewModel.updateActivePackage(symbolPackage.packageId) },
                                    enabled = !isGuest
                                ) {
                                    Text("Verwenden")
                                }

                                "Aktiv" -> Text(text = "Aktiv", color = Color.Green, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}