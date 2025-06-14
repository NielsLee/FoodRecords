package lying.fengfeng.foodrecords.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.ui.components.FoodRecordsBottomBar
import lying.fengfeng.foodrecords.ui.components.FoodRecordsTopBar
import lying.fengfeng.foodrecords.ui.components.SearchBottomSheet
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialog
import lying.fengfeng.foodrecords.ui.theme.FoodRecordsTheme
import lying.fengfeng.foodrecords.utils.EffectUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRecordsApp() {

    val appViewModel: FoodRecordsAppViewModel = viewModel()

    var showDialog by remember { appViewModel.isDialogShown }
    var showSearchDialog by remember { appViewModel.isSearchDialogShown }

    val navController = rememberNavController()
    val context = LocalContext.current

    val screenParams by remember { mutableStateOf(ScreenParams()) }
    val snackBarHostState = remember { SnackbarHostState() }

    val widthPixels = LocalContext.current.resources.displayMetrics.widthPixels
    val dpi = LocalContext.current.resources.displayMetrics.densityDpi
    val widthDp = widthPixels / (dpi / 160f)
    screenParams.widthDp = widthDp.dp
    screenParams.listColumnNum = if (widthDp > 600) 3 else 2
    screenParams.insertDialogWidthPercent = if (widthDp > 600) 0.6f else 1f
    val themeOption by remember{ appViewModel.themeOption }

    FoodRecordsTheme(
        themeOption = themeOption
    ) {
        CompositionLocalProvider(LocalScreenParams provides screenParams, LocalActivityContext provides context) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                },
                topBar = {
                    FoodRecordsTopBar(context.getString(R.string.app_name))
                },
                bottomBar = {
                    FoodRecordsBottomBar(
                        navController = navController,
                        fabOnClick = {
                            showDialog = true
                            EffectUtil.playSoundEffect(context)
                            EffectUtil.playVibrationEffect(context)
                        },
                        searchFabOnClick = {
                            showSearchDialog = true
                        }
                    )
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

                if (showSearchDialog) {
                    SearchBottomSheet(
                        onDismiss = {
                            showSearchDialog = false
                        },
                        onSearchFilterChange = { newFilterStr ->
                            appViewModel.filterStr.value = newFilterStr
                        },
                        snackBarHostState = snackBarHostState
                    )
                }
            }
        }
    }
}

val LocalScreenParams = compositionLocalOf<ScreenParams> {
    error("No LocalScreenParams provided")
}

val LocalActivityContext = compositionLocalOf<Context> {
    error("No ActivityContext provided")
}