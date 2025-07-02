package de.syntax_institut.androidabschlussprojekt.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.syntax_institut.androidabschlussprojekt.composables.BottomNavBar
import de.syntax_institut.androidabschlussprojekt.features.user.screens.FriendsScreen
import de.syntax_institut.androidabschlussprojekt.features.game.screens.InventoryScreen
import de.syntax_institut.androidabschlussprojekt.features.game.screens.LoadingScreen
import de.syntax_institut.androidabschlussprojekt.features.game.screens.LobbyScreen
import de.syntax_institut.androidabschlussprojekt.features.game.screens.LoginScreen
import de.syntax_institut.androidabschlussprojekt.features.game.screens.ShopScreen
import de.syntax_institut.androidabschlussprojekt.features.auth.viewModels.AuthViewModel

@Composable
fun AppStart(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val user by authViewModel.currentUser.collectAsState()
    val showLoading = remember { mutableStateOf(true) }

    val currentDestination = navController
        .currentBackStackEntryAsState().value?.destination?.route

    val isLoading by authViewModel.isChecking.collectAsState()
    val showBottomBar = currentDestination in listOf("lobby", "shop", "inventory", "friends")

    val backgroundColor = Color(0xFF083A8C)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        if (showLoading.value) {
            LoadingScreen(onLoadingComplete = { showLoading.value = false })
        } else {
            Scaffold(
                containerColor = backgroundColor,
                bottomBar = {
                    if (showBottomBar) {
                        BottomNavBar(navController = navController)
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = if (user != null) "lobby" else "login",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    composable("login") {
                        LoginScreen(navController = navController, authViewModel = authViewModel)
                    }
                    composable("lobby") {
                        LobbyScreen(navController = navController, authViewModel = authViewModel)
                    }
                    composable("shop") {
                        ShopScreen()
                    }
                    composable("inventory") {
                        InventoryScreen()
                    }
                    composable("friends") {
                        FriendsScreen(navController = navController, authViewModel = authViewModel)
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x88000000))
                        .zIndex(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}