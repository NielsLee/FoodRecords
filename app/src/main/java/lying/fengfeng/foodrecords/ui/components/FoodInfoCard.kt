package lying.fengfeng.foodrecords.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.TypeSpecimen
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.theme.ExpiredGreen
import lying.fengfeng.foodrecords.ui.theme.ExpiredRed
import lying.fengfeng.foodrecords.utils.DateUtil
import lying.fengfeng.foodrecords.utils.ImageUtil
import java.io.File
import kotlin.math.absoluteValue

@Composable
fun FoodInfoCard(
    foodInfo: FoodInfo,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val appViewModel: FoodRecordsAppViewModel =
        viewModel(viewModelStoreOwner = (context as MainActivity))
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val tipsButtonShown by remember { mutableStateOf(foodInfo.tips.isNotEmpty()) }
    var tipsShown by remember { mutableStateOf(false) }


    Card(
        modifier = modifier
            .padding(3.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (tipsButtonShown) {
                    // TODO 显示特别提醒/置顶
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                tipsShown = !tipsShown
                            }
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                val scrollState = rememberScrollState()
                Text(
                    text = foodInfo.foodName,
                    modifier = Modifier
                        .horizontalScroll(scrollState),
                    style = TextStyle(
                        fontSize = 24.sp
                    )
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val foodPicturePath = AppRepo.getPicturePath(foodInfo.uuid)

                Box {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap!!,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(0.6f)
                        )
                    }

                    // 在IO线程中加载图片
                    LaunchedEffect(Unit) {
                        val bitmap = ImageUtil.preProcessImage(foodPicturePath)
                        // 切换回主线程更新UI
                        launch(Dispatchers.Main) {
                            imageBitmap = bitmap?.asImageBitmap()
                        }
                    }
                }

                RemainingDaysWindow(
                    productionDate = foodInfo.productionDate,
                    shelfLife = foodInfo.shelfLife,
                    expirationDate = foodInfo.expirationDate,
                    isHorizontal = imageBitmap == null
                )
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {

            Column(
                Modifier.padding(horizontal = 8.dp).fillMaxWidth(0.7f)
            ) {
                val scrollState = rememberScrollState()

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.ShoppingCartCheckout, contentDescription = null)
                    Text(
                        text = foodInfo.amount.toString(),
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.horizontalScroll(scrollState)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.TypeSpecimen, contentDescription = null)
                    Text(
                        text = foodInfo.foodType,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.horizontalScroll(scrollState)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.DeleteForever, contentDescription = null)
                    Text(
                        text = DateUtil.getExpirationDate(foodInfo),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = modifier.weight(1f))

            Box(
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        dropDownMenuExpanded = true
                    },
                    modifier = Modifier
                ) {
                    Icon(Icons.Outlined.MoreHoriz, null, modifier = Modifier.size(36.dp))
                }

                DropdownMenu(
                    expanded = dropDownMenuExpanded,
                    onDismissRequest = { dropDownMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                Modifier.fillMaxSize()
                            ) {
                                Icon(imageVector = Icons.Outlined.Delete, null)
                                Text(text = context.getString(R.string.delete_record))
                            }
                        },
                        onClick = {
                            dropDownMenuExpanded = false
                            CoroutineScope(Dispatchers.IO).launch {
                                File(AppRepo.getPicturePath(foodInfo.uuid)).also {
                                    if (it.exists()) {
                                        it.delete()
                                    }
                                }
                                appViewModel.removeFoodInfo(foodInfo)
                            }
                        })
                }
            }
        }
    }
    
    AnimatedVisibility(
        visible = tipsShown,
        enter = slideInVertically(
            initialOffsetY = { it }
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { -it }
        ) + fadeOut()
        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f / 1f)
                    .animateContentSize()
                    .clickable { tipsShown = false },
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = foodInfo.tips,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RemainingDaysWindow(
    productionDate: String,
    shelfLife: String,
    expirationDate: String,
    isHorizontal: Boolean = false
) {
    val remainingDays = DateUtil.getRemainingDays(productionDate, shelfLife, expirationDate)
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val fontSize = 32.sp

        val (remainingTitleColor, remainingTitleText) = if (remainingDays > 0) {
            ExpiredGreen to context.getString(R.string.valid_in)
        } else {
            ExpiredRed to context.getString(R.string.expired)
        }

        val contents = @Composable {
            Box(
                modifier = Modifier
                    .border(2.dp, remainingTitleColor, shape = RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = remainingTitleText,
                    modifier = Modifier.padding(4.dp),
                    color = remainingTitleColor,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = remainingDays.let {
                    if (it.absoluteValue > 99) {
                        "99+"
                    } else {
                        it.absoluteValue.toString()
                    }
                },
                modifier = Modifier,
                style = TextStyle(
                    fontSize = fontSize,
                    color = remainingTitleColor
                )
            )

            Text(text = context.getString(R.string.shelf_life_day))
        }

        if (isHorizontal) {
            // No picture, use row to show remaining days
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .padding(horizontal = 1.dp)
                    .fillMaxWidth()
            ) {
                contents()
            }
        } else {
            // has picture, use column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                contents()
            }
        }
    }
}