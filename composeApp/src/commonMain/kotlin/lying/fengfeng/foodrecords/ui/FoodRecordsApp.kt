package lying.fengfeng.foodrecords.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import fridgey_kmf.composeapp.generated.resources.Res
import fridgey_kmf.composeapp.generated.resources.app_name
import lying.fengfeng.foodrecords.ui.components.FoodRecordsBottomBar
import lying.fengfeng.foodrecords.ui.components.FoodRecordsTopBar
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialog
import lying.fengfeng.foodrecords.ui.theme.FoodRecordsTheme
import lying.fengfeng.foodrecords.utils.EffectUtil
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodRecordsApp() {

    val appViewModel: FoodRecordsAppViewModel =
        viewModel(viewModelStoreOwner = AppViewModelOwner)

    var showDialog by remember { appViewModel.isDialogShown }
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }

    val themeOption by remember { appViewModel.themeOption }

    FoodRecordsTheme(
        themeOption = themeOption
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            topBar = {
                FoodRecordsTopBar(stringResource(Res.string.app_name))
            },
            bottomBar = {
                FoodRecordsBottomBar(
                    navController = navController,
                    fabOnClick = {
                        showDialog = true
                        EffectUtil.playSoundEffect()
                        EffectUtil.playVibrationEffect()
                    })
            }
        ) { paddingValues ->
            FoodRecordsNavHost(
                navController = navController,
                snackBarHostState = snackBarHostState,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )

            if (showDialog) {
                InsertionDialog(
                    shelfLifeList = appViewModel.shelfLifeList,
                    foodTypeList = appViewModel.foodTypeList,
                    onDismiss = {
                        showDialog = false
                    },
                    onFoodInfoCreated = {
                        appViewModel.addOrUpdateFoodInfo(it)
                    }
                )
            }
        }
    }
}

val LocalScreenParams = compositionLocalOf<ScreenParams> {
    error("No LocalScreenParams provided")
}