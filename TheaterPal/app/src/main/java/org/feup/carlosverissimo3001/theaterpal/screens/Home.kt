
package org.feup.carlosverissimo3001.theaterpal.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.feup.carlosverissimo3001.theaterpal.MyColors
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.shows.ShowDetails
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.shows.Shows

sealed class NavRoutes(val route: String) {
    data object Shows : NavRoutes("shows")
    data object Cafeteria : NavRoutes("cafeteria")
    data object ShowDetails : NavRoutes("showDetails")
    data object Wallet : NavRoutes("wallet")
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
            image = Icons.Outlined.Fastfood,
            route = "cafeteria"
        ),
        BarItem(
            title = "Shows",
            image = Icons.Outlined.TheaterComedy,
            route = "shows"
        ),
        BarItem(
            title = "Wallet",
            image = Icons.Outlined.Wallet,
            route = "wallet"
        )
    )
}

// The Navigator to be included as a Scaffold content, defining three possible destination routes
@Composable
fun Navigator(navController: NavHostController, ctx: Context) {
    NavHost(navController=navController, startDestination=NavRoutes.Shows.route) {
        composable(NavRoutes.Cafeteria.route) {
            Cafeteria(ctx, navController)
        }
        composable(NavRoutes.Shows.route) {
            Shows(ctx, navController)
        }
        composable(NavRoutes.Wallet.route) {
            Wallet(ctx, navController)
        }
        composable(NavRoutes.ShowDetails.route) {
            ShowDetails(ctx, navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var visible = remember { mutableStateOf(true) }

    // Hide the bottom navigation bar when navigating to the ShowDetails screen
    LaunchedEffect(key1 = navController.currentBackStackEntryAsState().value) {
        visible.value = currentRoute != NavRoutes.ShowDetails.route
    }
    AnimatedVisibility(visible = visible.value)
    {
        NavigationBar {
            NavBarItems.BarItems.forEach { navItem ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = navItem.image,
                            contentDescription = navItem.title
                        )
                    },
                    label = {
                        Text(
                            navItem.title,
                            style = TextStyle(
                                fontFamily = marcherFontFamily,
                            )
                        )
                    },
                    selected = currentRoute == navItem.route,
                    onClick = {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.findStartDestination().id)  // pops the stack until reaching the start destination (Home)
                            launchSingleTop =
                                true                                  // prevents duplicating the top stack entry (don't navigate to the current screen)
                        }
                    },
                    colors = NavigationBarItemColors(
                        selectedIconColor = Color.White,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MyColors.bottomNavBarUnselectedItemColor,
                        unselectedTextColor = MyColors.bottomNavBarUnselectedItemColor,
                        disabledIconColor = Color.Black,
                        disabledTextColor = Color.Black,
                    )
                )
            }
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