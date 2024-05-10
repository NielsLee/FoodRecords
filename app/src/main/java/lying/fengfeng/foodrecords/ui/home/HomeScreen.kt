package lying.fengfeng.foodrecords.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
        contentPadding = PaddingValues(3.dp),
        modifier = Modifier.background(
            Brush.verticalGradient(Pair(0.1f, MaterialTheme.colorScheme.primaryContainer), Pair(0.9f, Color.Transparent))
        )
    ) {

        items(
            count = foodInfoList.size,
            key = {
                foodInfoList[it].uuid
            }
        ) { index ->
            FoodInfoCard(foodInfo = foodInfoList[index], homeViewModel = homeViewModel, modifier = Modifier)
        }
    }
}