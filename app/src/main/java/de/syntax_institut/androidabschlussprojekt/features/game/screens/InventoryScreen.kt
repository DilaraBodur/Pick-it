package de.syntax_institut.androidabschlussprojekt.features.game.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.InventoryViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel = koinViewModel()
) {

    val packages by viewModel.ownedPackages.collectAsState()
    val activePackageId by viewModel.activePackageId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadInventory()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Inventar", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        if (packages.isEmpty()) {
            Text("Du hast keine gekauften Pakete.", color = Color.Gray)
        } else {
            LazyColumn {
                items(packages) { symbolPackage ->
                    val isActive = symbolPackage.packageId == activePackageId

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(symbolPackage.name, fontSize = 18.sp, fontWeight = FontWeight.Medium)

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

                            Button(
                                onClick = { if (!isActive) viewModel.updateActivePackage(symbolPackage.packageId) },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(text = if (isActive) "Aktiv" else "Verwenden")
                            }
                        }
                    }
                }
            }
        }
    }
}

