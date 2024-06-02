package lying.fengfeng.foodrecords.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.ui.components.FoodRecordsBottomBar
import lying.fengfeng.foodrecords.ui.components.FoodRecordsTopBar
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialog
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialogViewModel
import lying.fengfeng.foodrecords.ui.theme.FoodRecordsTheme
import lying.fengfeng.foodrecords.utils.EffectUtil

@Composable
fun FoodRecordsApp() {

    val dialogViewModel: InsertionDialogViewModel = viewModel()

    var showDialog by remember { dialogViewModel.isDialogShown }

    val navController = rememberNavController()
    val context = LocalContext.current

    val screenParams by remember { mutableStateOf(ScreenParams()) }

    val widthPixels = LocalContext.current.resources.displayMetrics.widthPixels
    val dpi = LocalContext.current.resources.displayMetrics.densityDpi
    val widthDp = widthPixels / (dpi / 160f)
    screenParams.listColumnNum = if (widthDp > 600) {
        3
    } else {
        2
    }
    screenParams.insertDialogWidthPercent = if (widthDp > 600) {
        0.6f
    } else {
        1f
    }

//    LaunchedEffect(showDialog) {
//        if (!showDialog) {
//            // 更新列表
//            MainScope().launch {
//                val newList = withContext(Dispatchers.IO) {
//                    AppRepo.getAllFoodInfo()
//                }
//                homeViewModel.updateList(newList)
//            }
//        }
//    }

    FoodRecordsTheme {
        CompositionLocalProvider(LocalScreenParams provides screenParams) {
            CompositionLocalProvider(LocalActivityContext provides context) {
                Scaffold(
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
                            })
                    }
                ) { paddingValues ->
                    FoodRecordsNavHost(
                        navController = navController,
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )

                    if (showDialog) {
                        InsertionDialog()
                    }
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