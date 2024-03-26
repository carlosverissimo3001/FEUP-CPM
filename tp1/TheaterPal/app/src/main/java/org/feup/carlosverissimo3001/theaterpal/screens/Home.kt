@file:OptIn(ExperimentalMaterial3Api::class)

package org.feup.carlosverissimo3001.theaterpal.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class NavRoutes(val route: String) {
    data object Shows : NavRoutes("shows")
    data object Cafeteria : NavRoutes("cafeteria")
    data object Transactions : NavRoutes("transactions")
}

data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: String
)

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Cafeteria",
            image = Icons.Outlined.LocalCafe,
            route = "cafeteria"
        ),
        BarItem(
            title = "Shows",
            image = Icons.Outlined.TheaterComedy,
            route = "shows"
        ),
        BarItem(
            title = "My Tickets",
            image = Icons.Outlined.ConfirmationNumber,
            route = "transactions"
        )
    )
}

// The first possible Scaffold content and a Navigation destination route
@Composable
fun Home() {
    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "home",
            tint = Color.Blue,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center)
        )
    }
}

// The second possible Scaffold content and a Navigation destination route
@Composable
fun Contacts() {
    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Filled.Face,
            contentDescription = "contacts",
            tint = Color.Blue,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center)
        )
    }
}

// The third possible Scaffold content and a Navigation destination route
@Composable
fun Favorites() {
    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "favorites",
            tint = Color.Blue,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center)
        )
    }
}

// The Navigator to be included as a Scaffold content, defining three possible destination routes
@Composable
fun Navigator(navController: NavHostController) {
    NavHost(navController=navController, startDestination=NavRoutes.Shows.route) {
        composable(NavRoutes.Shows.route) {
            Home()
        }
        composable(NavRoutes.Cafeteria.route) {
            Contacts()
        }
        composable(NavRoutes.Transactions.route) {
            Favorites()
        }
    }
}

// The Navigation bar to be included as the bottom bar of a Scaffold
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute==navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id)  // pops the stack until reaching the start destination (Home)
                        launchSingleTop = true                                  // prevents duplicating the top stack entry (don't navigate to the current screen)
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.image,
                        contentDescription = navItem.title
                    )
                },
                label = { Text(navItem.title) }
            )
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(Modifier.padding(it)) {
            Navigator(navController)
        }
    }
}