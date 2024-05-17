package lying.fengfeng.foodrecords.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.ui.LocalScreenParams
import lying.fengfeng.foodrecords.ui.components.FoodInfoCard

@Composable
fun HomeScreen(
    foodInfoList: List<FoodInfo>,
    onRefresh: () -> Unit
) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(LocalScreenParams.current.listColumnNum),
        contentPadding = PaddingValues(3.dp),
        modifier = Modifier
    ) {

        items(
            count = foodInfoList.size,
            key = {
                foodInfoList[it].uuid
            }
        ) { index ->
            FoodInfoCard(foodInfo = foodInfoList[index], modifier = Modifier, onDelete = onRefresh)
        }
    }
}