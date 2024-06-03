package lying.fengfeng.foodrecords.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import lying.fengfeng.foodrecords.ui.components.insertionDialog.createBitmap
import lying.fengfeng.foodrecords.ui.theme.ExpiredGreen
import lying.fengfeng.foodrecords.ui.theme.ExpiredRed
import lying.fengfeng.foodrecords.utils.DateUtil
import lying.fengfeng.foodrecords.utils.ImageUtil
import java.io.File

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


    Card(
        modifier = modifier
            .padding(3.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                // TODO 显示特别提醒/置顶
            }
            Text(
                text = foodInfo.foodName,
                modifier = Modifier
                    .padding(6.dp),
                style = TextStyle(
                    fontSize = 24.sp
                )
            )
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                ) {
                    Image(
                        bitmap = imageBitmap ?: createBitmap().asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                    )

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
                    shelfLife = foodInfo.shelfLife
                )
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {

            Column(
                Modifier.padding(horizontal = 8.dp)
            ) {

                Text(
                    text = foodInfo.foodType,
                    fontStyle = FontStyle.Italic
                )

                Text(
                    text = foodInfo.productionDate,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth(), // 设置为最大宽度 按钮才会显示在最右
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
}

@Composable
fun RemainingDaysWindow(
    productionDate: String,
    shelfLife: String,
) {
    val remainingDays = DateUtil.getRemainingDays(productionDate, shelfLife)
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val fontSize = 36.sp

        if (remainingDays > 0) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                Box(
                    modifier = Modifier
                        .border(2.dp, ExpiredGreen, shape = RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = context.getString(R.string.valid_in),
                        modifier = Modifier.padding(4.dp),
                        color = ExpiredGreen,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Text(
                    text = remainingDays.toString(),
                    modifier = Modifier,
                    style = TextStyle(
                        fontSize = fontSize,
                        color = ExpiredGreen
                    )
                )
                Text(text = context.getString(R.string.shelf_life_day))
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                Box(
                    modifier = Modifier
                        .border(2.dp, ExpiredRed, shape = RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = context.getString(R.string.expired),
                        modifier = Modifier.padding(4.dp),
                        color = ExpiredRed,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = (-remainingDays).toString(),
                    modifier = Modifier,
                    style = TextStyle(
                        fontSize = fontSize,
                        color = ExpiredRed
                    )
                )

                Text(text = context.getString(R.string.shelf_life_day))
            }
        }
    }
}