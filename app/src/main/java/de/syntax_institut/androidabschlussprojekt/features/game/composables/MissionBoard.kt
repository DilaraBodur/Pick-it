package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionItem
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionType
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.Symbol
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel

@Composable
fun MissionBoard(viewModel: GameViewModel) {
    val missions = viewModel.missionItems.collectAsState().value

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B007D))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            missions.filter { it.type == MissionType.THREE }
                .chunked(2).forEach { rowItems ->
                    MissionRow(rowItems)
                }

            Spacer(Modifier.height(8.dp))

            missions.filter { it.type == MissionType.JOKER }
                .chunked(2).forEach { rowItems ->
                    MissionRow(rowItems)
                }

            Spacer(Modifier.height(8.dp))

            missions.filter { it.type !in listOf(MissionType.THREE, MissionType.JOKER) }
                .chunked(2).forEach { rowItems ->
                    MissionRow(rowItems)
                }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MissionBoardPreview() {
    val dummyMissions = listOf(
        MissionItem("1", MissionType.THREE, Symbol(1, "❤️", "Herz", 300), true),
        MissionItem("2", MissionType.THREE, Symbol(2, "🌙", "Mond", 300), false),
        MissionItem("3", MissionType.THREE, Symbol(3, "⭐", "Stern", 300), true),
        MissionItem("4", MissionType.THREE, Symbol(4, "💎", "Diamant", 300), false),
        MissionItem("5", MissionType.THREE, Symbol(5, "👑", "Krone", 300), false),
        MissionItem("6", MissionType.THREE, Symbol(6, "☘️", "Kleeblatt", 300), false),
        MissionItem("joker1", MissionType.JOKER, null, false),
        MissionItem("joker2", MissionType.JOKER, null, false),
        MissionItem("fullhouse", MissionType.FULLHOUSE, null, true),
        MissionItem("five_diff", MissionType.FIVE_DIFF, null, false),
        MissionItem("fourer", MissionType.FOUR, null, false),
        MissionItem("fiver", MissionType.FIVE, null, false),
    )

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B007D))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            dummyMissions.filter { it.type == MissionType.THREE }
                .chunked(2).forEach { MissionRow(it) }

            Spacer(Modifier.height(8.dp))

            dummyMissions.filter { it.type == MissionType.JOKER }
                .chunked(2).forEach { MissionRow(it) }

            Spacer(Modifier.height(8.dp))

            dummyMissions.filter { it.type !in listOf(MissionType.THREE, MissionType.JOKER) }
                .chunked(2).forEach { MissionRow(it) }
        }
    }
}