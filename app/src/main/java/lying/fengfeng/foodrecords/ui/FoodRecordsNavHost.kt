package lying.fengfeng.foodrecords.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import lying.fengfeng.foodrecords.ui.home.HomeScreen
import lying.fengfeng.foodrecords.ui.home.HomeViewModel
import lying.fengfeng.foodrecords.ui.list.ListScreen
import lying.fengfeng.foodrecords.ui.settings.SettingsScreen

@Composable
fun FoodRecordsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val homeViewModel: HomeViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(homeViewModel)
        }
        composable("list") {
            ListScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}