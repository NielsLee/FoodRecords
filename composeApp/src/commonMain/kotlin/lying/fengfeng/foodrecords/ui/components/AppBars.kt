package lying.fengfeng.foodrecords.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ImportExport
import androidx.compose.material3.Badge
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fridgey_kmf.composeapp.generated.resources.Res
import fridgey_kmf.composeapp.generated.resources.dice3_svg
import fridgey_kmf.composeapp.generated.resources.export_data
import fridgey_kmf.composeapp.generated.resources.import_data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.backup.BackupLauncherDelegate
import lying.fengfeng.foodrecords.ui.AppViewModelFactory
import lying.fengfeng.foodrecords.ui.AppViewModelOwner
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.utils.DateUtil
import lying.fengfeng.foodrecords.utils.EffectUtil
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRecordsTopBar(title: String) {

//    val activity = LocalContext.current as MainActivity
    var currentDate by remember { mutableStateOf(DateUtil.getCurrentDate()) }
    var isDropdownMenuExpanded by remember { mutableStateOf(false) }


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = title)
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp)
                )
                Text(text = currentDate)
                IconButton(onClick = { isDropdownMenuExpanded = !isDropdownMenuExpanded }) {
                    Icon(imageVector = Icons.Outlined.ImportExport, contentDescription = null)
                }
                DropdownMenu(
                    expanded = isDropdownMenuExpanded,
                    onDismissRequest = {
                        isDropdownMenuExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                Modifier.fillMaxSize()
                            ) {
                                Text(stringResource(resource = Res.string.import_data))
                            }
                        },
                        onClick = {
                            BackupLauncherDelegate.launchImportLauncher()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(
                                Modifier.fillMaxSize()
                            ) {
                                Text(stringResource(resource = Res.string.export_data))
                            }
                        },
                        onClick = {
                            BackupLauncherDelegate.launchExportLauncher()
                        }
                    )
                }
            }
        },
        modifier = Modifier.shadow(12.dp)
    )

    LaunchedEffect(key1 = Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    currentDate = DateUtil.getCurrentDate()
                }
            }
        }
    }
}

@Composable
fun FoodRecordsBottomBar(
    navController: NavController,
    fabOnClick: () -> Unit
) {

    val appViewModel: FoodRecordsAppViewModel = viewModel(viewModelStoreOwner = AppViewModelOwner, factory = AppViewModelFactory())
    val isNewUITried by remember { appViewModel.isNewUITried }

    BottomAppBar(
        actions = {
            IconButton(onClick = {
                if (navController.currentBackStackEntry?.destination?.route != "home") {
                    navController.navigate("home")
                    EffectUtil.playSoundEffect()
                    EffectUtil.playVibrationEffect()
                }
            }) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Icon navigate to Home page"
                )
            }
            IconButton(onClick = {
                if (navController.currentBackStackEntry?.destination?.route != "dice") {
                    navController.navigate("dice")
                    EffectUtil.playSoundEffect()
                    EffectUtil.playVibrationEffect()
                }
            }) {
                Icon(
                    painter = painterResource(resource = Res.drawable.dice3_svg),
                    "Icon navigate to Dice page"
                )
            }
            IconButton(onClick = {
                if (navController.currentBackStackEntry?.destination?.route != "settings") {
                    navController.navigate("settings")
                    EffectUtil.playSoundEffect()
                    EffectUtil.playVibrationEffect()
                }
            }) {
                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    Badge(containerColor = if (isNewUITried) Color.Transparent else Color.Red)
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Icon navigate to Settings Page",
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = fabOnClick,
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        },
        modifier = Modifier
    )
}