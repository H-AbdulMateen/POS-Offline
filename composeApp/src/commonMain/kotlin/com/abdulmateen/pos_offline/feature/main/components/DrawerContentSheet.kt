package com.abdulmateen.pos_offline.feature.main.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdulmateen.pos_offline.feature.main.MainScreenRoutes
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.home
import pos_offline.composeapp.generated.resources.inventory
import pos_offline.composeapp.generated.resources.logout
import pos_offline.composeapp.generated.resources.profile
import pos_offline.composeapp.generated.resources.settings


@Composable
fun DrawerContentSheet(
    navController: NavHostController,
    hierarchy: Sequence<NavDestination>?,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean,
    onLogoutClick: () -> Unit,
    closeDrawer: () -> Unit
) {

    ModalDrawerSheet{
        Spacer(modifier = Modifier.height(16.dp))
        DrawerItem(stringResource(Res.string.home),
            icon = Icons.Default.Home,
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Home::class) } == true
        ) {
            navController.navigate(MainScreenRoutes.Home) { launchSingleTop = true }
            closeDrawer()
        }
        Spacer(modifier = Modifier.height(16.dp))
        DrawerItem(stringResource(Res.string.inventory),
            icon = Icons.Default.Inventory,
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Inventory::class) } == true
        ) {
            navController.navigate(MainScreenRoutes.Inventory) { launchSingleTop = true }
            closeDrawer()
        }
        Spacer(modifier = Modifier.height(16.dp))
        DrawerItem("Expenses",
            icon = Icons.AutoMirrored.Filled.ReceiptLong,
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Expenses::class) } == true
        ) {
            navController.navigate(MainScreenRoutes.Expenses) { launchSingleTop = true }
            closeDrawer()
        }
        Spacer(modifier = Modifier.height(16.dp))
        DrawerItem(stringResource(Res.string.profile),
            icon = Icons.Default.Person,
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Profile::class) } == true
        ) {
            navController.navigate(MainScreenRoutes.Profile) { launchSingleTop = true }
            closeDrawer()
        }
        Spacer(modifier = Modifier.height(16.dp))
        DrawerItem(
            title = stringResource(Res.string.settings),
            icon = Icons.Default.Settings,
            selected = hierarchy?.any { it.hasRoute(MainScreenRoutes.Settings::class) } == true
        ) {
            navController.navigate(MainScreenRoutes.Settings) { launchSingleTop = true }
            closeDrawer()
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Dark Mode",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isDarkTheme, onCheckedChange = { toggleDarkTheme() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surface
                ))
        }

        Spacer(modifier = Modifier.weight(1f))

        DrawerItem(
            title = stringResource(Res.string.logout),
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            selected = false,
            isDanger = true
        ) {
            onLogoutClick()
            closeDrawer()
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    isDanger: Boolean = false,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isDanger) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    NavigationDrawerItem(
        label = {
            Text(title, color = color, style = MaterialTheme.typography.bodyLarge)
        },
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = title, tint = color) }
    )
}

@Preview(name = "Light Mode")
@Composable
private fun DrawerContentSheetPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            DrawerContentSheet(
                navController = rememberNavController(),
                hierarchy = null,
                toggleDarkTheme = {},
                isDarkTheme = false,
                onLogoutClick = {},
                closeDrawer = {}
            )
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
private fun DrawerContentSheetDarkPreview(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            DrawerContentSheet(
                navController = rememberNavController(),
                hierarchy = null,
                toggleDarkTheme = {},
                isDarkTheme = false,
                onLogoutClick = {},
                closeDrawer = {}
            )
        }
    )
}