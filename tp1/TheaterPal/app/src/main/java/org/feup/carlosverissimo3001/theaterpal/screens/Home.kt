
package org.feup.carlosverissimo3001.theaterpal.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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

// The Navigator to be included as a Scaffold content, defining three possible destination routes
@Composable
fun Navigator(navController: NavHostController, ctx: Context) {
    NavHost(navController=navController, startDestination=NavRoutes.Shows.route) {
        composable(NavRoutes.Shows.route) {
            Shows(ctx)
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
fun MainScreen(ctx: Context) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(Modifier.padding(it)) {
            Navigator(navController, ctx)
        }
    }
}