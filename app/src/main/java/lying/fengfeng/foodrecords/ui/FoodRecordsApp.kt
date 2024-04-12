package lying.fengfeng.foodrecords.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import lying.fengfeng.foodrecords.entities.FoodInfo
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

    val foodInfoList = listOf(
        FoodInfo("FoodName1", "2022-02-02", "CXasdfasdfasdfasdfaK", "7dayfs", "uuid"),
        FoodInfo("FoodNamsdfe2asdfasdfasdfasdfasdfzxcvzxcvzxcvzxcvze", "2022-02-02", "CXK", "asdfasd7days", "uuid"),
        FoodInfo("me3", "2022-02-02", "CXasdfasadfaK", "7days", "uuid"),
        FoodInfo("Fooame4", "2022-02-02", "CXK", "7ys", "uuid"),
        FoodInfo("FoodNamdgd", "2022-02-02", "CXKasd", "7days", "uuid"),
        FoodInfo("FoodNamsdfe2asdfasdfasdfasdfasdfzxcvzxcvzxcvzxcvze", "2022-02-02", "CXK", "asdfasd7days", "uuid"),
        )

    var foodInfoIndex = 0

    FoodRecordsTheme {
        Scaffold(
            topBar = {
                FoodRecordsTopBar()
            },
            bottomBar = {
                FoodRecordsBottomBar(
                    navController = navController,
                    fabOnClick = {
//                        showDialog = true
                        homeViewModel.updateList(foodInfoList[foodInfoIndex++])
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