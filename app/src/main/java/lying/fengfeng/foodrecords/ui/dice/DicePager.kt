package lying.fengfeng.foodrecords.ui.dice

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.repository.FoodInfoRepo
import lying.fengfeng.foodrecords.ui.components.FoodInfoCard
import lying.fengfeng.foodrecords.utils.EffectUtil
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DicePager(

) {

    val context = LocalContext.current

    // TODO 全局用一套cardDataList
    var cardDataList: List<FoodInfo> by remember {
        mutableStateOf(listOf())
    }

    LaunchedEffect(key1 = cardDataList) {
        withContext(Dispatchers.IO) {
            cardDataList = FoodInfoRepo.getAll()
        }
    }

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        val pagerState = rememberPagerState(pageCount = {
            cardDataList.size
        })

        Box(
            modifier = Modifier.fillMaxSize(0.6f),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                FoodInfoCard(foodInfo = cardDataList[page])
            }
        }

        val coroutineScope = rememberCoroutineScope()
        var isRollButtonEnabled by remember { mutableStateOf(true) }

        IconButton(
            onClick = {
                coroutineScope.launch {
                    isRollButtonEnabled = false
                    val pageList = selectRandomElements(cardDataList, 13)
                    val smoothList = generateDeceleratedSequence(
                        100,
                        800,
                        pageList.size,
                        0.75
                    )
                    for ((pageIndex, pageValue) in pageList.withIndex()) {
                        delay(smoothList[pageIndex].toLong())

                        val indexOfCard = cardDataList.indexOf(pageValue)
                        pagerState.scrollToPage(
                            page = indexOfCard
                        )

                        EffectUtil.playSoundEffect(context)
                        EffectUtil.playVibrationEffect(context)

                    }
                    isRollButtonEnabled = true
                    EffectUtil.playNotification(context)
                }

            },
            modifier = Modifier
                .wrapContentSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            enabled = isRollButtonEnabled
        ) {
            Icon(painter = painterResource(id = R.drawable.dice5_svg), null)
        }
    }
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

fun <T> selectRandomElements(givenList: List<T>, count: Int): List<T> {
    val selectedList = mutableListOf<T>()
    val random = Random(System.currentTimeMillis())

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