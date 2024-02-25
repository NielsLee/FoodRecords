package lying.fengfeng.foodrecords.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import lying.fengfeng.foodrecords.ui.components.FoodRecordsBottomBar
import lying.fengfeng.foodrecords.ui.components.FoodRecordsTopBar
import lying.fengfeng.foodrecords.ui.components.InsertionDialog
import lying.fengfeng.foodrecords.ui.home.HomeViewModel
import lying.fengfeng.foodrecords.ui.theme.FoodRecordsTheme

@Composable
fun FoodRecordsApp() {

    val viewModel: HomeViewModel = viewModel()

    var showDialog by remember { mutableStateOf(false) }

    val navController = rememberNavController()

    FoodRecordsTheme {
        Scaffold(
            topBar = {
                FoodRecordsTopBar()
            },
            bottomBar = {
                FoodRecordsBottomBar(
                    navController = navController,
                    fabOnClick = {
                        showDialog = true
                        viewModel.updateList(888)
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
                InsertionDialog {
                    showDialog = false
                }
            }
        }
    }
}