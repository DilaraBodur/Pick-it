package de.syntax_institut.androidabschlussprojekt.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import de.syntax_institut.androidabschlussprojekt.features.navigation.BottomNavItem

@Composable
fun BottomNavBar(
    navController: NavController
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
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = Color.White
                ) },
                label = { Text(
                    item.label,
                    color = Color.White
                    )
                }
            )
        }
    }
}

