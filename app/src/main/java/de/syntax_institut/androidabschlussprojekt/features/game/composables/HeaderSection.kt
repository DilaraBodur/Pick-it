package de.syntax_institut.androidabschlussprojekt.features.game.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.game.viewModels.GameViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun HeaderSection(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {

    val buttonWidth = 150.dp

    val headerButtonSizeModifier = Modifier
        .width(buttonWidth)
        .height(56.dp)
        .padding(horizontal = 12.dp, vertical = 6.dp)

    val bonusProgress = viewModel.bonusProgress.collectAsState().value
    val requiredPoints = viewModel.requiredPoints.collectAsState().value
    val currentPoints = viewModel.currentPoints.collectAsState().value
    val roundBonus = viewModel.roundBonus.collectAsState().value
    val totalPoints = viewModel.totalPoints.collectAsState().value
    val progress by viewModel.progress


    val authViewModel: AuthViewModel = koinViewModel()
    val username = authViewModel.currentUserModel.collectAsState().value?.username ?: "Gast"

    LaunchedEffect(Unit) {
        viewModel.startTimer()
    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ExitButton(onExit = { viewModel.openExitDialog() }, modifier = headerButtonSizeModifier)

            TimeProgressBar(
                progress = progress,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(start = 8.dp)
            )

            Box(
                modifier = headerButtonSizeModifier
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.7f),
                            topLeft = Offset(6f, 6f),
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(30f)
                        )
                    }
                    .background(
                        color = Color(0xFF1565C0),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = headerButtonSizeModifier
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.7f),
                            topLeft = Offset(6f, 6f),
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(30f)
                        )
                    }
                    .background(
                        color = Color(0xFF1565C0),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                RoundInfo(viewModel)
            }

            BonusProgressBar(
                progress = bonusProgress,
                currentPoints = currentPoints,
                requiredPoints = requiredPoints,
                rondBonus = roundBonus,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(start = 8.dp)
            )

            Box(
                modifier = headerButtonSizeModifier
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.7f),
                            topLeft = Offset(6f, 6f),
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(30f)
                        )
                    }
                    .background(
                        color = Color(0xFF1565C0),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$totalPoints",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}