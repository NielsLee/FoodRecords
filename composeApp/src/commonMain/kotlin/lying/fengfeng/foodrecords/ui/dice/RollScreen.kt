package lying.fengfeng.foodrecords.ui.dice

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fridgey_kmf.composeapp.generated.resources.Res
import fridgey_kmf.composeapp.generated.resources.dice5_svg
import fridgey_kmf.composeapp.generated.resources.only_one
import fridgey_kmf.composeapp.generated.resources.pick_it
import fridgey_kmf.composeapp.generated.resources.roll_title_primary
import fridgey_kmf.composeapp.generated.resources.roll_title_secondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.painterResource
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.ui.AppViewModelOwner
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.utils.EffectUtil
import lying.fengfeng.foodrecords.utils.ToastUtil
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RollScreen() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        val coroutineScope = rememberCoroutineScope()
        val appViewModel: FoodRecordsAppViewModel =
            viewModel(AppViewModelOwner)
        val foodInfoList: List<FoodInfo> = remember { appViewModel.foodInfoList }
        val pagerState = rememberPagerState(pageCount = {
            foodInfoList.size
        })
        var isRollButtonEnabled by remember { mutableStateOf(foodInfoList.isNotEmpty()) }

        val onlyOneStr = stringResource(resource = Res.string.only_one)
        val pickItStr = stringResource(resource = Res.string.pick_it)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(resource = Res.string.roll_title_primary),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Text(
                text = stringResource(resource = Res.string.roll_title_secondary),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            DicePager(pagerState)
        }

        IconButton(
            onClick = {
                if (foodInfoList.size == 1) {
                    ToastUtil.show(onlyOneStr)
                    return@IconButton
                }
                coroutineScope.launch {
                    isRollButtonEnabled = false
                    val pageList = selectRandomElements(foodInfoList, 13)
                    val smoothList = generateDeceleratedSequence(
                        100,
                        800,
                        pageList.size,
                        0.75
                    )
                    for ((pageIndex, pageValue) in pageList.withIndex()) {
                        delay(smoothList[pageIndex].toLong())

                        val indexOfCard = foodInfoList.indexOf(pageValue)
                        pagerState.scrollToPage(
                            page = indexOfCard
                        )

                        EffectUtil.playSoundEffect()
                        EffectUtil.playVibrationEffect()

                    }
                    isRollButtonEnabled = true
                    EffectUtil.playNotification()
                    ToastUtil.show(pickItStr)
                }
            },
            modifier = Modifier
                .padding(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            enabled = isRollButtonEnabled
        ) {
            Icon(painter = painterResource(resource = Res.drawable.dice5_svg), null)
        }
    }
}

fun <T> selectRandomElements(givenList: List<T>, count: Int): List<T> {
    val selectedList = mutableListOf<T>()
    val random = Random(Clock.System.now().toEpochMilliseconds())

    if (givenList.size < count) {
        selectedList.addAll(givenList)
    }

    val remainingCount = count - selectedList.size
    for (i in 0 until remainingCount) {
        val randomIndex = random.nextInt(givenList.size)
        selectedList.add(givenList[randomIndex])
    }

    selectedList.shuffle(random)

    return selectedList
}

fun generateDeceleratedSequence(min: Int, max: Int, count: Int, percent: Double): List<Int> {
    if (min >= max) throw IllegalArgumentException("Min should be less than Max.")
    if (percent < 0.0 || percent > 1.0) throw IllegalArgumentException("Percent must be between 0 and 1.")
    if (count <= 0) throw IllegalArgumentException("Count must be positive.")

    val sequence = MutableList(count) { min }
    val startDecelerationIndex = (percent * count).toInt()
    val decelerationLength = count - startDecelerationIndex

    for (i in 0 until decelerationLength) {
        val decelerationFactor = i.toDouble() / (decelerationLength - 1) // 计算减速因子
        sequence[startDecelerationIndex + i] = min + ((max - min) * decelerationFactor).toInt()
    }

    return sequence
}