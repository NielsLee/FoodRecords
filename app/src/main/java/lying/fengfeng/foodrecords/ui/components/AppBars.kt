package lying.fengfeng.foodrecords.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ImportExport
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.utils.EffectUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRecordsTopBar(title: String) {

    val activity = LocalContext.current as MainActivity
    var currentDate by remember { mutableStateOf(getCurrentDate()) }
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
                                Text(text = activity.getString(R.string.import_data))
                            }
                        },
                        onClick = {
                            activity.importLauncher.launch("")
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(
                                Modifier.fillMaxSize()
                            ) {
                                Text(text = activity.getString(R.string.export_data))
                            }
                        },
                        onClick = {
                            activity.exportLauncher.launch("")
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
                    currentDate = getCurrentDate()
                }
            }
        }
    }
}

@Composable
fun FoodRecordsBottomBar(
    navController: NavController,
    fabOnClick: () -> Unit,
    searchFabOnClick: ()-> Unit
) {

    val context = LocalContext.current
    val appViewModel: FoodRecordsAppViewModel = viewModel(context as MainActivity)
    val isNewUITried by remember { appViewModel.isNewUITried }

    BottomAppBar(
        actions = {
            IconButton(onClick = {
                if (navController.currentBackStackEntry?.destination?.route != "home") {
                    navController.navigate("home")
                    EffectUtil.playSoundEffect(context)
                    EffectUtil.playVibrationEffect(context)
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
                    EffectUtil.playSoundEffect(context)
                    EffectUtil.playVibrationEffect(context)
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.dice3_svg),
                    "Icon navigate to Dice page"
                )
            }
            IconButton(onClick = {
                if (navController.currentBackStackEntry?.destination?.route != "settings") {
                    navController.navigate("settings")
                    EffectUtil.playSoundEffect(context)
                    EffectUtil.playVibrationEffect(context)
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
            Row {
                FloatingActionButton(
                    modifier = Modifier.padding(2.dp),
                    onClick = searchFabOnClick,
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(Icons.Filled.Search, contentDescription = "search")
                }
                FloatingActionButton(
                    modifier = Modifier.padding(2.dp),
                    onClick = fabOnClick,
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(Icons.Filled.Add, "Localized description")
                }
            }

        },
        modifier = Modifier
    )
}

private fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat(AppRepo.getDateFormat(), Locale.getDefault())
    return dateFormat.format(Date())
}