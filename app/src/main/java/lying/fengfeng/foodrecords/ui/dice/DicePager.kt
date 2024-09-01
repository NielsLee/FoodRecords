package lying.fengfeng.foodrecords.ui.dice

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.LocalActivityContext
import lying.fengfeng.foodrecords.ui.components.FoodInfoCard
import lying.fengfeng.foodrecords.ui.components.FoodInfoCardNew

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DicePager(
    pagerState: PagerState
) {

    val activityContext = LocalActivityContext.current
    val appViewModel: FoodRecordsAppViewModel = viewModel(viewModelStoreOwner = (activityContext as MainActivity))
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
    Text(
        text = stringResource(id = R.string.dice_view_empty_title),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
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