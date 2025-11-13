package com.abdulmateen.cmpskeleton.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abdulmateen.cmpskeleton.feature.auth.navigation.AuthNavGraph
import com.abdulmateen.cmpskeleton.feature.main.MainScreenRoot
import com.abdulmateen.cmpskeleton.feature.main.home.presentation.product_detail.ProductDetailScreenRoot

@Composable
fun AppNavGraph(
    isLoggedIn: Boolean,
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) RootScreenRoutes.Main else RootScreenRoutes.AuthGraph
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
                navigateToProductDetail = {
                    navController.navigate(RootScreenRoutes.ProductDetail(productId = it))
                },
                onLogoutClick = { navController.navigate(RootScreenRoutes.AuthGraph){
                    popUpTo(0) {
                        inclusive = true
                    }
                }
                }
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
    }
}