package lying.fengfeng.foodrecords.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.ui.dice.RollScreen
import lying.fengfeng.foodrecords.ui.home.HomeScreen
import lying.fengfeng.foodrecords.ui.settings.SettingsScreen
import lying.fengfeng.foodrecords.utils.DateUtil

val routeList = listOf("home", "dice", "settings")

@Composable
fun FoodRecordsNavHost(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier
) {

    val activityContext = LocalActivityContext.current
    val appViewModel: FoodRecordsAppViewModel = viewModel(viewModelStoreOwner = (activityContext as MainActivity))
    val foodInfoList = remember { appViewModel.foodInfoList }.sortedBy { it.getSortIndex() }

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier.background(
            Brush.verticalGradient(Pair(0.1f, MaterialTheme.colorScheme.primaryContainer), Pair(0.9f, Color.Transparent))
        ),
        enterTransition = {
            slideIntoContainer(getSlideDirection(initialState, targetState), spring())
        },
        exitTransition = {
            slideOutOfContainer(getSlideDirection(initialState, targetState), spring())
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(400)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(400)
            )
        }
    ) {
        composable(routeList[0]) {
            HomeScreen(
                foodInfoList = foodInfoList
            )
        }
        composable(routeList[1]) {
            RollScreen()
        }
        composable(routeList[2]) {
            SettingsScreen(snackBarHostState)
        }
    }
}

fun getSlideDirection(initialState: NavBackStackEntry, targetState: NavBackStackEntry):
        AnimatedContentTransitionScope.SlideDirection {
    val sourceIndex = routeList.indexOf(initialState.destination.route)
    val targetIndex = routeList.indexOf(targetState.destination.route)
    return if (targetIndex > sourceIndex) {
        AnimatedContentTransitionScope.SlideDirection.Start
    } else {
        AnimatedContentTransitionScope.SlideDirection.End
    }
}