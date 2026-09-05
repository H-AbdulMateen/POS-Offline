package com.abdulmateen.pos_offline.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abdulmateen.pos_offline.feature.auth.navigation.AuthNavGraph
import com.abdulmateen.pos_offline.feature.main.MainScreenRoot
import com.abdulmateen.pos_offline.feature.home.presentation.CartScreenRoot
import com.abdulmateen.pos_offline.feature.home.presentation.product_detail.ProductDetailScreenRoot
import com.abdulmateen.pos_offline.feature.setup.presentation.SetupScreenRoot

@Composable
fun AppNavGraph(
    isLoggedIn: Boolean,
    isSetupCompleted: Boolean,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
){
    val navController = rememberNavController()
    val startDestination = when {
        !isSetupCompleted -> RootScreenRoutes.Setup
//        !isLoggedIn -> RootScreenRoutes.AuthGraph
        else -> RootScreenRoutes.Main
    }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable<RootScreenRoutes.Setup> {
            SetupScreenRoot(
                onSetupComplete = {
                    navController.navigate(RootScreenRoutes.Main) {
                        popUpTo(RootScreenRoutes.Setup) { inclusive = true }
                    }
                }
            )
        }
        composable<RootScreenRoutes.AuthGraph>{
            AuthNavGraph(
                navigateToMain = {
                    navController.navigate(RootScreenRoutes.Main) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<RootScreenRoutes.Main>(
            exitTransition = { slideOutHorizontally() },
            popEnterTransition = { slideInHorizontally() }
        ){
            MainScreenRoot(
                navigateToCart = {
                    navController.navigate(RootScreenRoutes.Cart)
                },
                onLogoutClick = { navController.navigate(RootScreenRoutes.AuthGraph){
                    popUpTo(0) {
                        inclusive = true
                    }
                }
                },
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme
            )
        }
        composable<RootScreenRoutes.ProductDetail>(
            enterTransition = {
                slideInHorizontally { initialOffset ->
                    initialOffset
                }
            },
            exitTransition = {
                slideOutHorizontally { initialOffset ->
                    initialOffset
                }
            }
        ) {
            ProductDetailScreenRoot(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<RootScreenRoutes.Cart>(
            enterTransition = {
                slideInHorizontally { initialOffset ->
                    initialOffset
                }
            },
            exitTransition = {
                slideOutHorizontally { initialOffset ->
                    initialOffset
                }
            }
        ) {
            CartScreenRoot(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}