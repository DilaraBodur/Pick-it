package de.syntax_institut.androidabschlussprojekt.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import de.syntax_institut.androidabschlussprojekt.features.navigation.BottomNavItem

@Composable
fun BottomNavBar(
    navController: NavController,
    showFriendsDialogState: MutableState<Boolean>
) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        BottomNavItem(route = "lobby", icon = Icons.Default.Home, label = "Startseite"),
        BottomNavItem(route = "shop", icon = Icons.Default.ShoppingCart, label = "Laden"),
        BottomNavItem(route = "inventory", icon = Icons.Default.Inventory, label = "Inventar"),
        BottomNavItem(route = "friends", icon = Icons.Default.People, label = "Freunde")
    )

    NavigationBar(
        containerColor = Color(0xFF083A8C)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (item.route == "friends") {
                        showFriendsDialogState.value = true
                    } else if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(
                    imageVector = item.icon,
                    contentDescription = item.label
                ) },
                label = { Text(
                    item.label
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Gray,
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}

