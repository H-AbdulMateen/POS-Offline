package com.abdulmateen.pos_offline.feature.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.feature.main.components.DrawerContentSheet
import com.abdulmateen.pos_offline.feature.main.home.presentation.order.CreateOrderScreenRoot
import com.abdulmateen.pos_offline.feature.main.profile.presentation.ProfileScreenRoot
import com.abdulmateen.pos_offline.feature.main.settings.presentation.SettingsScreenRoot
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.app_name

@Composable
fun MainScreenRoot(
    navigateToProductDetail: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    MainScreen(
        navigateToProductDetail = navigateToProductDetail,
        onLogoutClick = onLogoutClick,
        toggleDarkTheme = toggleDarkTheme,
        isDarkTheme = isDarkTheme
    )
}

@Composable
fun MainScreen(
    navigateToProductDetail: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    val navController = rememberNavController()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    when(deviceConfiguration){
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            MainScreenScaffold(
                navController = navController,
                navigateToProductDetail = navigateToProductDetail,
                onLogoutClick = onLogoutClick,
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme
            )
        }
        DeviceConfiguration.MOBILE_LANDSCAPE,
             -> {
            MainScreenScaffoldWithDrawer(
                navController = navController,
                navigateToProductDetail = navigateToProductDetail,
                onLogoutClick = onLogoutClick,
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme
            )
        }
        DeviceConfiguration.TABLET_PORTRAIT -> {
            MainScreenScaffold(
                navController = navController,
                navigateToProductDetail = navigateToProductDetail,
                onLogoutClick = onLogoutClick,
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme
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
                        navController = navController,
                        toggleDarkTheme = toggleDarkTheme,
                        isDarkTheme = isDarkTheme
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
    onLogoutClick: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
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
                navController = navController,
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenScaffoldWithDrawer(
    navController: NavHostController,
    navigateToProductDetail: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            DrawerContentSheet(
                navController = navController,
                hierarchy = navController.currentBackStackEntryAsState().value?.destination?.hierarchy,
                onLogoutClick = onLogoutClick,
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    title = {
                        Text(text = stringResource(Res.string.app_name))
                    },
                    actions = {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notification",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if(isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Search",
                                modifier = Modifier.size(24.dp)
                                    .clickable(
                                        onClick = toggleDarkTheme
                                    )
                            )
                        }
                    }
                )
            }
        ){ paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .padding(paddingValues)
            ) {
                NavHostPane(
                    navigateToProductDetail = navigateToProductDetail,
                    onLogoutClick = onLogoutClick,
                    navController = navController,
                    toggleDarkTheme = toggleDarkTheme,
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}




@Composable
fun NavHostPane(
    navigateToProductDetail: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    navController: NavHostController,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean,
    drawerToggle: () -> Unit = {}
){
    NavHost(
        navController = navController,
        startDestination = MainScreenRoutes.Home,
        ) {
        composable<MainScreenRoutes.Home>() {
            CreateOrderScreenRoot(
                navigateToProductDetail = navigateToProductDetail,
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme
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
        onLogoutClick = {},
        toggleDarkTheme = {},
        isDarkTheme = false
    )
}