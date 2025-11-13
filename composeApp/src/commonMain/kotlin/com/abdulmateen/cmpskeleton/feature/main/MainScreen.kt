package com.abdulmateen.cmpskeleton.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abdulmateen.cmpskeleton.core.utils.DeviceConfiguration
import com.abdulmateen.cmpskeleton.feature.main.home.presentation.product_list.ProductListScreenRoot
import com.abdulmateen.cmpskeleton.feature.main.profile.presentation.ProfileScreenRoot
import com.abdulmateen.cmpskeleton.feature.main.settings.presentation.SettingsScreenRoot
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MainScreenRoot(
    navigateToProductDetail: (Int) -> Unit,
    onLogoutClick: () -> Unit
) {
    MainScreen(
        navigateToProductDetail = navigateToProductDetail,
        onLogoutClick = onLogoutClick
    )
}

@Composable
fun MainScreen(
    navigateToProductDetail: (Int) -> Unit,
    onLogoutClick: () -> Unit
) {
    val navController = rememberNavController()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    when(deviceConfiguration){
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE,
             -> {
            MainScreenScaffold(
                navController = navController,
                navigateToProductDetail = navigateToProductDetail,
                onLogoutClick = onLogoutClick
            )
        }
        DeviceConfiguration.TABLET_PORTRAIT -> {
            MainScreenScaffold(
                navController = navController,
                navigateToProductDetail = navigateToProductDetail,
                onLogoutClick = onLogoutClick
            )
        }
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                SideNavBar(
                    hierarchy = navController.currentBackStackEntryAsState().value?.destination?.hierarchy,
                    navController = navController
                )
                Box(
                    modifier = Modifier.fillMaxWidth().weight(.1f)
                ){
                    NavHostPane(
                        navigateToProductDetail = navigateToProductDetail,
                        onLogoutClick = onLogoutClick,
                        navController = navController
                    )
                }
            }

        }
    }
}

@Composable
fun MainScreenScaffold(
    navController: NavHostController,
    navigateToProductDetail: (Int) -> Unit,
    onLogoutClick: () -> Unit
){
    Scaffold(
        bottomBar = {
            BottomNavBar(
                hierarchy = navController.currentBackStackEntryAsState().value?.destination?.hierarchy,
                navController = navController
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .consumeWindowInsets(WindowInsets.navigationBars)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            NavHostPane(
                navigateToProductDetail = navigateToProductDetail,
                onLogoutClick = onLogoutClick,
                navController = navController
            )
        }
    }
}

@Composable
fun NavHostPane(
    navigateToProductDetail: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = MainScreenRoutes.Home,

        ) {
        composable<MainScreenRoutes.Home>() {
            ProductListScreenRoot(
                navigateToProductDetail = navigateToProductDetail
            )
        }
        composable<MainScreenRoutes.Profile>() {
            ProfileScreenRoot()
        }
        composable<MainScreenRoutes.Settings>() {
            SettingsScreenRoot(
                onLogoutClick = onLogoutClick
            )
        }

    }
}

@Composable
fun BottomNavBar(
    hierarchy: Sequence<NavDestination>?,
    navController: NavController
) {
    NavigationBar {
        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Home::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Home, "emails") },
            onClick = { navController.navigate(MainScreenRoutes.Home) }
        )
        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Profile::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Person, "profile") },
            onClick = { navController.navigate(MainScreenRoutes.Profile) }
        )
        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Settings::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Settings, "settings") },
            onClick = { navController.navigate(MainScreenRoutes.Settings) }
        )
    }
}
@Composable
fun SideNavBar(
    hierarchy: Sequence<NavDestination>?,
    navController: NavController
) {
    NavigationRail {
        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Home::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Home, "emails") },
            onClick = { navController.navigate(MainScreenRoutes.Home) }
        )
        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Profile::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Person, "profile") },
            onClick = { navController.navigate(MainScreenRoutes.Profile) }
        )
        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Settings::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Settings, "settings") },
            onClick = { navController.navigate(MainScreenRoutes.Settings) }
        )
    }
}



sealed interface MainScreenRoutes {
    @Serializable
    data object Home : MainScreenRoutes

    @Serializable
    data object Profile : MainScreenRoutes

    @Serializable
    data object Settings : MainScreenRoutes
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        navigateToProductDetail = {},
        onLogoutClick = {}
    )
}