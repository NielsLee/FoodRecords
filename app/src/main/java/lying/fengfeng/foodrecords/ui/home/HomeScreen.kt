package lying.fengfeng.foodrecords.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lying.fengfeng.foodrecords.ui.components.FoodInfoCard

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
) {

    val foodInfoList by remember {
        homeViewModel.foodInfoList
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
    ) {

        val padding = 6.dp

        items(foodInfoList.size) { index ->
            if (index % 2 == 0) {
                FoodInfoCard(foodInfo = foodInfoList[index], modifier = Modifier.padding(start = padding, top = padding))
            } else {
                FoodInfoCard(foodInfo = foodInfoList[index], modifier = Modifier.padding(start = padding, top = padding, end = padding))
            }
        }
    }
}