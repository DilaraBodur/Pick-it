package de.syntax_institut.androidabschlussprojekt.features.user.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import de.syntax_institut.androidabschlussprojekt.features.user.data.models.User
import de.syntax_institut.androidabschlussprojekt.composables.CountryDropdown

@Composable
fun EditProfileDialog(
    user: User,
    availableAvatars: List<String>,
    onSave: (String, String, String?) -> Unit,
    onDismiss: () -> Unit
) {
    var username by remember { mutableStateOf(user.username) }
    var selectedCountry by remember { mutableStateOf(user.countryCode) }

    var selectedAvatar by remember {
        mutableStateOf(user.photoUrl?.substringAfter("seed=") ?: "")
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text("Profil bearbeiten", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Name bearbeiten") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                CountryDropdown(
                    selectedCountry = selectedCountry,
                    onCountrySelected = { selected ->
                        selectedCountry = selected
                    }
                )

                Spacer(Modifier.height(12.dp))

                Text("Avatar:", style = MaterialTheme.typography.bodyMedium)

                Log.d("AVATAR", "Anzahl: ${availableAvatars.size}")
                availableAvatars.forEach { Log.d("AVATAR", it) }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    items(availableAvatars) { seed ->

                        val isSelected = selectedAvatar == seed
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color.Blue else Color.Gray,
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                                .clickable { selectedAvatar = seed }
                        ) {
                            AsyncImage(
                                model = "https://api.dicebear.com/7.x/adventurer/png?seed=$seed",
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val avatarSeed: String? = if (selectedAvatar.startsWith("https://")) {
                            null
                        } else {
                            selectedAvatar
                        }

                        onSave(username, selectedCountry, avatarSeed)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Speichern")
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Abbrechen",
                        color = Color.Red)
                }
            }
        }
    }
}