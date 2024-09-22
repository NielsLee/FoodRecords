package lying.fengfeng.foodrecords.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import lying.fengfeng.foodrecords.AppContextDelegate
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.ui.AppViewModelOwner
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.LocalScreenParams
import lying.fengfeng.foodrecords.ui.components.FoodInfoCard
import lying.fengfeng.foodrecords.ui.components.FoodInfoCardNew

@Composable
fun HomeScreen(
    foodInfoList: List<FoodInfo>
) {

    val appViewModel: FoodRecordsAppViewModel = viewModel(viewModelStoreOwner = AppViewModelOwner)
    val isNewUI by remember { appViewModel.isNewUI }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(LocalScreenParams.current.getListColumnCount()),
        contentPadding = PaddingValues(3.dp),
        modifier = Modifier
    ) {

        items(
            count = foodInfoList.size,
            key = {
                foodInfoList[it].uuid
            }
        ) { index ->
            if (isNewUI) {
                FoodInfoCardNew(
                    foodInfo = foodInfoList[index],
                    modifier = Modifier
                )
            } else {
                FoodInfoCard(
                    foodInfo = foodInfoList[index],
                    modifier = Modifier
                )
            }
        }
    }
}