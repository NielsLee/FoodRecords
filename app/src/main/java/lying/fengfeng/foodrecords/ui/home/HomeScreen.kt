package lying.fengfeng.foodrecords.ui.home

import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
    ) {
        items(foodInfoList.size) { index ->
            FoodInfoCard(foodInfo = foodInfoList[index], modifier = Modifier) {
                CoroutineScope(Dispatchers.IO).launch {
                    File(FoodInfoRepo.getPicturePath(foodInfoList[index].pictureUUID)).also {
                        if (it.exists()) {
                            it.delete()
                        }
                    }
                    FoodInfoRepo.remove(foodInfoList[index])
                    homeViewModel.updateList(FoodInfoRepo.getAll())
                }
            }
        }
    }
}