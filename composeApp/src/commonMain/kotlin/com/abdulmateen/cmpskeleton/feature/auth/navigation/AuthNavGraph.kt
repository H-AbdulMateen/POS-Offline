package com.abdulmateen.cmpskeleton.feature.auth.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abdulmateen.cmpskeleton.feature.auth.presentation.login.LoginScreenRoot
import com.abdulmateen.cmpskeleton.feature.auth.presentation.register.SignUpScreenRoot

@Composable
fun AuthNavGraph(
    navigateToMain: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AuthScreenRoutes.Login
    ) {
        composable<AuthScreenRoutes.Login>(
            exitTransition = { slideOutHorizontally() },
            popEnterTransition = { slideInHorizontally() }
        ) {
            LoginScreenRoot(
                onLoginSuccess = {
                    navigateToMain()
                },
                navigateToSignUp = {
                    navController.navigate(AuthScreenRoutes.SignUp)
                }
            )
        }

        composable<AuthScreenRoutes.SignUp>(
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
            SignUpScreenRoot(
                onBackClicked = {
                    navController.navigateUp()
                }
            )
        }
    }
}