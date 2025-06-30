package de.syntax_institut.androidabschlussprojekt.ui.dialog

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import de.syntax_institut.androidabschlussprojekt.R

@Composable
fun SettingsDialog(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    isMusicOn: Boolean,
    onMusicToggle: (Boolean) -> Unit,
    isSoundOn: Boolean,
    onSoundToggle: (Boolean) -> Unit,
    areNotificationsOn: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    onLinkGoogle: () -> Unit,
    onLinkFacebook: () -> Unit,
    isGuest: Boolean,
    isGoogleUser: Boolean,
    isFacebookUser: Boolean,
    linkGoogleLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    facebookLinkingView: @Composable () -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf("Deutsch", "English", "Türkçe")
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current


    if (isGuest) {
        Button(onClick = {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                linkGoogleLauncher.launch(signInIntent)
            }
        }) {
            Text("Mit Google verknüpfen")
        }

        Spacer(modifier = Modifier.height(8.dp))

        facebookLinkingView()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Schließen")
            }
        },
        title = {
            Text("Einstellungen", fontWeight = FontWeight.Bold)
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 800.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Sprache auswählen:")
                    Box {
                        OutlinedButton(onClick = { expanded = true }) {
                            Text(currentLanguage)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            languages.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang) },
                                    onClick = {
                                        onLanguageChange(lang)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Musik")
                        Spacer(Modifier.weight(1f))
                        Switch(checked = isMusicOn, onCheckedChange = onMusicToggle)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Geräuscheffekte")
                        Spacer(Modifier.weight(1f))
                        Switch(checked = isSoundOn, onCheckedChange = onSoundToggle)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Benachrichtigungen")
                        Spacer(Modifier.weight(1f))
                        Switch(checked = areNotificationsOn, onCheckedChange = onNotificationsToggle)
                    }

                    HorizontalDivider()

                    when {
                        isGuest -> {
                            Button(
                                onClick = onLogout,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Logout")
                            }
                            Button(onClick = onLinkGoogle, modifier = Modifier.fillMaxWidth()) {
                                Text("Mit Google verknüpfen")
                            }
                            Button(onClick = onLinkFacebook, modifier = Modifier.fillMaxWidth()) {
                                Text("Mit Facebook verknüpfen")
                            }
                        }
                        isGoogleUser -> {
                            Button(
                                onClick = onDeleteAccount,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ),
                                modifier = Modifier.fillMaxWidth()) {
                                Text("Konto löschen")
                            }
                            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                                Text("Von Google abmelden")
                            }
                            Button(onClick = onLinkFacebook, modifier = Modifier.fillMaxWidth()) {
                                Text("Mit Facebook verknüpfen")
                            }
                        }
                        isFacebookUser -> {
                            Button(
                                onClick = onDeleteAccount,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ),
                                modifier = Modifier.fillMaxWidth()) {
                                Text("Konto löschen")
                            }
                            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                                Text("Von Facebook abmelden")
                            }
                            Button(onClick = onLinkGoogle, modifier = Modifier.fillMaxWidth()) {
                                Text("Mit Google verknüpfen")
                            }
                        }
                    }
                }
            }
        }
    )
}