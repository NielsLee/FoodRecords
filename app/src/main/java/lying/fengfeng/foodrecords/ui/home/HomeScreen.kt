package lying.fengfeng.foodrecords.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lying.fengfeng.foodrecords.ui.components.FoodInfoCard

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
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

class customArrangement : Arrangement.Vertical {

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        outPositions: IntArray
    ) {
        var leftColumnHeight = 0
        var rightColumnHeight = 0
        val columnWidth = totalSize / 2 // 假设是两列布局

        var leftPosition = 0
        var rightPosition = 0

        for (i in sizes.indices) {
            if (i % 2 == 0) {
                // 左侧的元素
                // 元素高度
                leftColumnHeight += sizes[i]
                outPositions[i] = leftPosition
                leftPosition += leftColumnHeight
            } else {
                // 右侧的元素
                rightColumnHeight += sizes[i]
                outPositions[i] = rightPosition
                rightPosition += rightColumnHeight
            }

            if (i % 2 == 1 || i == sizes.size - 1) {
                leftColumnHeight = 0
                rightColumnHeight = 0
            }
        }
    }
}