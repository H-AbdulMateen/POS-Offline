package com.abdulmateen.pos_offline.feature.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.abdulmateen.pos_offline.core.designsystem.components.CartBadgeBox
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.feature.main.components.DrawerContentSheet
import com.abdulmateen.pos_offline.feature.home.presentation.order.OrderScreenRoot
import com.abdulmateen.pos_offline.feature.dashboard.presentation.DashboardScreenRoot
import com.abdulmateen.pos_offline.feature.expense.presentation.ExpenseScreenRoot
import com.abdulmateen.pos_offline.feature.profile.presentation.ProfileScreenRoot
import com.abdulmateen.pos_offline.feature.settings.presentation.SettingsScreenRoot
import com.abdulmateen.pos_offline.feature.credit.presentation.CreditScreenRoot
import com.abdulmateen.pos_offline.feature.home.presentation.order.OrderHistoryScreenRoot
import com.abdulmateen.pos_offline.feature.return_module.presentation.ReturnScreenRoot
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.AssignmentReturn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.app_name
import pos_offline.composeapp.generated.resources.dashboard
import pos_offline.composeapp.generated.resources.home
import pos_offline.composeapp.generated.resources.inventory
import pos_offline.composeapp.generated.resources.profile
import pos_offline.composeapp.generated.resources.settings

@Composable
fun MainScreenRoot(
    navigateToCart: () -> Unit,
    onLogoutClick: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    val viewModel = koinViewModel<MainViewModel>()
    MainScreen(
        navigateToCart = navigateToCart,
        onLogoutClick = onLogoutClick,
        toggleDarkTheme = toggleDarkTheme,
        isDarkTheme = isDarkTheme,
        itemsCount = viewModel.cartCount.value
    )
}

@Composable
fun MainScreen(
    navigateToCart: () -> Unit,
    onLogoutClick: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean,
    itemsCount: Int = 0
) {
    val navController = rememberNavController()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    
    when(deviceConfiguration){
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE,
        DeviceConfiguration.TABLET_PORTRAIT -> {
            MainScreenScaffoldWithDrawer(
                navController = navController,
                navigateToCart = navigateToCart,
                onLogoutClick = onLogoutClick,
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme,
                itemsCount = itemsCount
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
                        navController = navController,
                        navigateToCart = navigateToCart,
                        toggleDarkTheme = toggleDarkTheme,
                        isDarkTheme = isDarkTheme,
                        onLogoutClick = onLogoutClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenScaffoldWithDrawer(
    navController: NavHostController,
    navigateToCart: () -> Unit,
    onLogoutClick: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean,
    itemsCount: Int
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val title = when {
        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.Home::class) } == true -> stringResource(Res.string.home)
        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.Inventory::class) } == true -> stringResource(Res.string.inventory)
        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.Dashboard::class) } == true -> stringResource(Res.string.dashboard)
        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.Expenses::class) } == true -> "Expenses"
        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.Credits::class) } == true -> "Credits"
        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.OrderHistory::class) } == true -> "Order History"
        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.Returns::class) } == true -> "Returns"
//        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.Profile::class) } == true -> stringResource(Res.string.profile)
        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.Settings::class) } == true -> stringResource(Res.string.settings)
        currentDestination?.hierarchy?.any { it.hasRoute(MainScreenRoutes.Reports::class) } == true -> "Reports"
        else -> stringResource(Res.string.app_name)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContentSheet(
                navController = navController,
                hierarchy = currentDestination?.hierarchy,
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
                        Text(text = title)
                    },
                    actions = {
                        Row {
                            CartBadgeBox(
                                itemCount = itemsCount,
                                onCartClick = navigateToCart
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if(isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Theme Toggle",
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
                    .padding(paddingValues)
            ) {
                NavHostPane(
                    navController = navController,
                    navigateToCart = navigateToCart,
                    toggleDarkTheme = toggleDarkTheme,
                    isDarkTheme = isDarkTheme,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
fun NavHostPane(
    navController: NavHostController,
    navigateToCart: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean,
    onLogoutClick: () -> Unit
){
    NavHost(
        navController = navController,
        startDestination = MainScreenRoutes.Home,
        ) {
        composable<MainScreenRoutes.Home>() {
            OrderScreenRoot(
                navigateToCart = navigateToCart,
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme
            )
        }
        composable<MainScreenRoutes.Inventory> {
            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryScreenRoot()
        }
        composable<MainScreenRoutes.Dashboard> {
            DashboardScreenRoot()
        }
        composable<MainScreenRoutes.Expenses> {
            ExpenseScreenRoot()
        }
        composable<MainScreenRoutes.Credits> {
            CreditScreenRoot()
        }
        composable<MainScreenRoutes.OrderHistory> {
            OrderHistoryScreenRoot(onBackClick = { navController.popBackStack() })
        }
        composable<MainScreenRoutes.Returns> {
            ReturnScreenRoot()
        }
//        composable<MainScreenRoutes.Profile>() {
//            ProfileScreenRoot()
//        }
        composable<MainScreenRoutes.Settings>() {
            SettingsScreenRoot(
                onLogoutClick = onLogoutClick
            )
        }
        composable<MainScreenRoutes.Reports> {
            com.abdulmateen.pos_offline.feature.reports.presentation.ReportsScreenRoot()
        }
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
            icon = { Icon(imageVector = Icons.Default.Home, "home") },
            label = { Text(text = stringResource(Res.string.home)) },
            onClick = { navController.navigate(MainScreenRoutes.Home) }
        )

        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Dashboard::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Dashboard, "dashboard") },
            label = { Text(text = stringResource(Res.string.dashboard)) },
            onClick = { navController.navigate(MainScreenRoutes.Dashboard) }
        )

        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Inventory::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Inventory, contentDescription = "inventory") },
            label = { Text(text = stringResource(Res.string.inventory)) },
            onClick = { navController.navigate(MainScreenRoutes.Inventory) }
        )

        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Expenses::class) } == true,
            icon = { Icon(imageVector = Icons.AutoMirrored.Filled.ReceiptLong, "expenses") },
            label = { Text(text = "Expenses") },
            onClick = { navController.navigate(MainScreenRoutes.Expenses) }
        )

        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Credits::class) } == true,
            icon = { Icon(imageVector = Icons.Default.CreditCard, contentDescription = "credits") },
            label = { Text(text = "Credits") },
            onClick = { navController.navigate(MainScreenRoutes.Credits) }
        )

        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.OrderHistory::class) } == true,
            icon = { Icon(imageVector = Icons.Default.History, contentDescription = "history") },
            label = { Text(text = "Orders") },
            onClick = { navController.navigate(MainScreenRoutes.OrderHistory) }
        )

        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Returns::class) } == true,
            icon = { Icon(imageVector = Icons.Default.AssignmentReturn, contentDescription = "returns") },
            label = { Text(text = "Returns") },
            onClick = { navController.navigate(MainScreenRoutes.Returns) }
        )

        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Profile::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Person, "profile") },
            label = { Text(text = stringResource(Res.string.profile)) },
            onClick = { navController.navigate(MainScreenRoutes.Profile) }
        )
        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Settings::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Settings, "settings") },
            label = { Text(text = stringResource(Res.string.settings)) },
            onClick = { navController.navigate(MainScreenRoutes.Settings) }
        )
        NavigationRailItem(
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Reports::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Assessment, "reports") },
            label = { Text(text = "Reports") },
            onClick = { navController.navigate(MainScreenRoutes.Reports) }
        )
    }
}



sealed interface MainScreenRoutes {
    @Serializable
    data object Home : MainScreenRoutes

    @Serializable
    data object Inventory: MainScreenRoutes

    @Serializable
    data object Dashboard : MainScreenRoutes

    @Serializable
    data object Expenses : MainScreenRoutes

    @Serializable
    data object Credits : MainScreenRoutes

    @Serializable
    data object OrderHistory : MainScreenRoutes

    @Serializable
    data object Returns : MainScreenRoutes

    @Serializable
    data object Profile : MainScreenRoutes

    @Serializable
    data object Settings : MainScreenRoutes

    @Serializable
    data object Reports : MainScreenRoutes
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        navigateToCart = {},
        onLogoutClick = {},
        toggleDarkTheme = {},
        isDarkTheme = false
    )
}
