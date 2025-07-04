package de.syntax_institut.androidabschlussprojekt.features.game.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import de.syntax_institut.androidabschlussprojekt.R
import de.syntax_institut.androidabschlussprojekt.composables.AvatarImage
import de.syntax_institut.androidabschlussprojekt.composables.BottomNavBar
import de.syntax_institut.androidabschlussprojekt.features.user.screens.EditProfileDialog
import de.syntax_institut.androidabschlussprojekt.features.user.screens.ProfileDialog
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel
import de.syntax_institut.androidabschlussprojekt.features.user.friends.screens.FriendsDialog
import de.syntax_institut.androidabschlussprojekt.features.user.friends.viewModels.FriendsViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val callbackManager = remember { CallbackManager.Factory.create() }

    val userModel = authViewModel.currentUserModel.collectAsState().value
    val username = userModel?.username?.takeIf { it.isNotBlank() } ?: "Unbekannt"
    val avatarUrl = userModel?.photoUrl

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRankingSheet by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }

    var selectedLanguage by remember { mutableStateOf("Deutsch") }
    var isMusicEnabled by remember { mutableStateOf(true) }
    var isSoundEnabled by remember { mutableStateOf(true) }
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    val showProfileDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }

    val showFriendsDialog = remember { mutableStateOf(false) }

    val availableAvatars = listOf(
        "lion", "bear", "cat", "fox", "panda",
        "zebra", "giraffe", "elephant", "tiger", "koala",
        "monkey", "owl", "wolf", "deer", "dog",
        "rabbit", "penguin", "sloth", "cow", "duck"
    )

    val isGuest = userModel?.loginProvider == "firebase"
    val isGoogleUser = userModel?.loginProvider == "google.com"
    val isFacebookUser = userModel?.loginProvider == "facebook.com"

    val currentUser by authViewModel.currentUserModel.collectAsState()
    val friendsViewModel: FriendsViewModel = koinViewModel()

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            friendsViewModel.startListeningToFriends(uid)
        }
    }

    val linkGoogleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)

                val idToken = account.idToken
                val displayName = account.displayName
                val email = account.email
                val photoUrl = account.photoUrl?.toString()

                if (idToken != null) {
                    authViewModel.linkWithGoogle(
                        idToken = idToken,
                        displayName = displayName,
                        email = email,
                        photoUrl = photoUrl
                    )
                    showSettings = false
                }
            } catch (e: ApiException) {
                Log.e("LobbyScreen", "Fehler bei Google Account Auswahl: ${e.message}")
            }
        }
    )

    val facebookLinkingView: @Composable () -> Unit = {
        AndroidView(factory = {
            LoginButton(it).apply {
                setPermissions("email", "public_profile")
                registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        authViewModel.linkWithFacebook(result.accessToken)
                    }

                    override fun onCancel() {}

                    override fun onError(error: FacebookException) {
                        Log.e("LobbyScreen", "Facebook-Link fehlgeschlagen: ${error.message}")
                    }
                })
            }
        })
    }

    if (showSettings) {
        SettingsDialog(
            currentLanguage = selectedLanguage,
            onLanguageChange = { selectedLanguage = it },
            isMusicOn = isMusicEnabled,
            onMusicToggle = { isMusicEnabled = it },
            isSoundOn = isSoundEnabled,
            onSoundToggle = { isSoundEnabled = it },
            areNotificationsOn = isNotificationsEnabled,
            onNotificationsToggle = { isNotificationsEnabled = it },
            isGuest = isGuest,
            linkGoogleLauncher = linkGoogleLauncher,
            facebookLinkingView = facebookLinkingView,
            onDismiss = { showSettings = false },
            onLogout = { authViewModel.logout() },
            onDeleteAccount = { authViewModel.deleteAccount() },
            onLinkGoogle = {
                val googleSignInClient = GoogleSignIn.getClient(
                    context,
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                )
                linkGoogleLauncher.launch(googleSignInClient.signInIntent)
            },
            onLinkFacebook = { },
            isGoogleUser = isGoogleUser,
            isFacebookUser = isFacebookUser
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF083A8C))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!avatarUrl.isNullOrBlank()) {
                AvatarImage(
                    url = avatarUrl,
                    onClick = { showProfileDialog.value = true }
                )
            }

            Button(onClick = { showRankingSheet = true }) {
                Text("Rangliste")
            }

            IconButton(onClick = {
                showSettings = true
            }) {
                Icon(Icons.Default.Settings, contentDescription = "Einstellungen", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Online", "2 vs 2", "Solo").forEach { mode ->
                    GameModeCard(
                        title = mode,
                        backgroundColor = Color(0xFF1565C0),
                        onClick = {
                            when (mode) {
                                "Online" -> { /* TODO: Online implementieren */ }
                                "2 vs 2" -> { /* TODO: 2 vs 2 implementieren */ }
                                "Solo" -> navController.navigate("game_solo")
                            }
                        },
                        modifier = Modifier
                    )
                }
            }
        }

        BottomNavBar(
            navController = navController,
            showFriendsDialogState = showFriendsDialog
        )
    }

    if (showRankingSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRankingSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Rangliste", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                // TODO: Ranglisten daten einbauen
                repeat(10) { i ->
                    Text(text = "${i + 1}. Spielername - Punkte", color = Color.White)
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konto wirklich löschen?") },
            text = { Text("Dieser Vorgang kann nicht rückgängig gemacht werden.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    authViewModel.deleteAccount()
                    navController.navigate("login") {
                        popUpTo("lobby") { inclusive = true }
                    }
                }) {
                    Text("Löschen", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }

    if (showProfileDialog.value) {
        ProfileDialog(
            user = userModel!!,
            onDismiss = { showProfileDialog.value = false },
            onEditClick = {
                showProfileDialog.value = false
                showEditDialog.value = true
            }
        )
    }

    if (showEditDialog.value) {
        EditProfileDialog(
            user = userModel!!,
            availableAvatars = availableAvatars,
            onSave = { name, country, avatar ->
                authViewModel.updateUserProfile(name, country, avatar)
                showEditDialog.value = false
            },
            onDismiss = { showEditDialog.value = false }
        )
    }

    if (showFriendsDialog.value) {
        FriendsDialog(
            authViewModel = authViewModel,
            friendsViewModel = koinViewModel<FriendsViewModel>(),
            onDismiss = { showFriendsDialog.value = false }
        )
    }
}