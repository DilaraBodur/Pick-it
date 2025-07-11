package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.syntax_institut.androidabschlussprojekt.features.game.data.models.MissionItem

@Composable
fun MissionRow(rowItems: List<MissionItem>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(vertical = 4.dp)
    ) {
        rowItems.forEach { mission ->
            MissionItemBox(mission, Modifier.height(40.dp).weight(1f))
        }

        if (rowItems.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}