package lying.fengfeng.foodrecords.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import lying.fengfeng.foodrecords.ui.compose.FoodRecordsBottomBar
import lying.fengfeng.foodrecords.ui.compose.FoodRecordsTopBar
import lying.fengfeng.foodrecords.ui.compose.InsertionDialogCompose
import lying.fengfeng.foodrecords.ui.home.HomeViewModel
import lying.fengfeng.foodrecords.ui.theme.FoodRecordsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRecordsApp() {

    val viewModel: HomeViewModel = viewModel()

    val showDialog = remember {
        mutableStateOf(false)
    }

    val navController = rememberNavController()

    FoodRecordsTheme {
        Scaffold(
            topBar = {
                FoodRecordsTopBar()
            },
            bottomBar = {
                FoodRecordsBottomBar(navController = navController, fabOnClick = {
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

            if (showDialog.value) {
                InsertionDialogCompose.InsertionDialog {
                    showDialog.value = false
                }
            }
        }
    }
}