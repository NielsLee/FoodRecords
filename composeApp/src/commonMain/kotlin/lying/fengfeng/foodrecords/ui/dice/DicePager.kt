package lying.fengfeng.foodrecords.ui.dice

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fridgey_kmf.composeapp.generated.resources.Res
import fridgey_kmf.composeapp.generated.resources.dice_view_empty_title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.ui.AppViewModelOwner
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.components.FoodInfoCard
import lying.fengfeng.foodrecords.ui.components.FoodInfoCardNew

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DicePager(
    pagerState: PagerState
) {

    val appViewModel: FoodRecordsAppViewModel = viewModel(viewModelStoreOwner = AppViewModelOwner)
    var isCardListEmpty by remember { mutableStateOf(false) }

    var foodInfoList: List<FoodInfo> = remember { appViewModel.foodInfoList }
    val isNewUI by remember { appViewModel.isNewUI }

    LaunchedEffect(key1 = foodInfoList) {
        withContext(Dispatchers.IO) {
            foodInfoList = AppRepo.getAllFoodInfo()
        }
        isCardListEmpty = foodInfoList.isEmpty()
    }
    
    if (isCardListEmpty) {
        EmptyView()
    } else {
        DiceView(cardDataList = foodInfoList, pagerState = pagerState, isNewUI = isNewUI)
    }
}

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(resource = Res.string.dice_view_empty_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiceView(cardDataList: List<FoodInfo>, pagerState: PagerState, isNewUI: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isNewUI) {
                        FoodInfoCardNew(foodInfo = cardDataList[page])
                    } else {
                        FoodInfoCard(foodInfo = cardDataList[page], modifier = Modifier.fillMaxWidth(0.6f))
                    }
                }
            }
        }

    }
}