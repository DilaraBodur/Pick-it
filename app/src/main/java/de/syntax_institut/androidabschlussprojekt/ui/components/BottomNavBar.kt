package de.syntax_institut.androidabschlussprojekt.ui.components

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

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        BottomNavItem("Startseite", Icons.Default.Home, "lobby"),
        BottomNavItem("Laden", Icons.Default.ShoppingCart, "shop"),
        BottomNavItem("Inventar", Icons.Default.Inventory, "inventory"),
        BottomNavItem("Freunde", Icons.Default.People, "friends")
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
                            popUpTo("lobby") { inclusive = false }
                            launchSingleTop = true
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

