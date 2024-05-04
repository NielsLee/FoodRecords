package lying.fengfeng.foodrecords.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.repository.FoodInfoRepo
import lying.fengfeng.foodrecords.ui.components.FoodRecordsBottomBar
import lying.fengfeng.foodrecords.ui.components.FoodRecordsTopBar
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialog
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialogViewModel
import lying.fengfeng.foodrecords.ui.home.HomeViewModel
import lying.fengfeng.foodrecords.ui.theme.FoodRecordsTheme

@Composable
fun FoodRecordsApp() {

    val homeViewModel: HomeViewModel = viewModel()
    val dialogViewModel: InsertionDialogViewModel = viewModel()

    var showDialog by remember { dialogViewModel.isDialogShown }

    val navController = rememberNavController()
    val mContext = LocalContext.current

    LaunchedEffect(showDialog) {
        if (!showDialog) {
            // 更新列表
            MainScope().launch {
                val newList = withContext(Dispatchers.IO) {
                    FoodInfoRepo.getAll()
                }
                homeViewModel.updateList(newList)
            }
        }
    }

    FoodRecordsTheme {
        Scaffold(
            topBar = {
                FoodRecordsTopBar(mContext.getString(R.string.app_name))
            },
            bottomBar = {
                FoodRecordsBottomBar(
                    navController = navController,
                    fabOnClick = {
                        showDialog = true
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