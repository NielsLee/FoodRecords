package lying.fengfeng.foodrecords.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.repository.FoodInfoRepo
import lying.fengfeng.foodrecords.ui.components.FoodInfoCard
import java.io.File

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
) {

    val foodInfoList by remember {
        homeViewModel.foodInfoList
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(3.dp)
    ) {
        items(foodInfoList.size) { index ->
            FoodInfoCard(foodInfo = foodInfoList[index], homeViewModel = homeViewModel, modifier = Modifier)
        }
    }
}