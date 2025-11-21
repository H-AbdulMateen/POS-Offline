package com.abdulmateen.pos_offline.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abdulmateen.pos_offline.feature.auth.navigation.AuthNavGraph
import com.abdulmateen.pos_offline.feature.main.MainScreenRoot
import com.abdulmateen.pos_offline.feature.main.home.presentation.CartScreen
import com.abdulmateen.pos_offline.feature.main.home.presentation.product_detail.ProductDetailScreenRoot

@Composable
fun AppNavGraph(
    isLoggedIn: Boolean,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
//        startDestination = if (isLoggedIn) RootScreenRoutes.Main else RootScreenRoutes.AuthGraph
        startDestination = RootScreenRoutes.Main
    ){
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
            CartScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}