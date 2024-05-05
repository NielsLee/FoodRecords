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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.repository.FoodInfoRepo
import lying.fengfeng.foodrecords.ui.components.insertionDialog.createBitmap
import lying.fengfeng.foodrecords.ui.home.HomeViewModel
import lying.fengfeng.foodrecords.utils.DateUtil
import lying.fengfeng.foodrecords.utils.ImageUtil
import java.io.File

@Composable
fun FoodInfoCard(
    foodInfo: FoodInfo,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {

    val mContext = LocalContext.current
    var dropDownMenuExpanded by remember { mutableStateOf(false) }


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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier

            ) {
                val foodPicturePath = FoodInfoRepo.getPicturePath(foodInfo.pictureUUID)
                val bitmap = ImageUtil.preProcessImage(foodPicturePath)

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                ) {
                    Image(
                        bitmap = bitmap?.asImageBitmap() ?: createBitmap().asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
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
                modifier = Modifier.fillMaxSize(),
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
                                Text(text = mContext.getString(R.string.delete_record))
                            }
                        },
                        onClick = {
                            dropDownMenuExpanded = false
                            CoroutineScope(Dispatchers.IO).launch {
                                File(FoodInfoRepo.getPicturePath(foodInfo.pictureUUID)).also {
                                    if (it.exists()) {
                                        it.delete()
                                    }
                                }
                                FoodInfoRepo.remove(foodInfo)
                                homeViewModel.updateList(FoodInfoRepo.getAll())
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
    val mContext = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (remainingDays > 0) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .border(2.dp, Color.Green, shape = RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = mContext.getString(R.string.best_before),
                        modifier = Modifier.padding(4.dp),
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = remainingDays.toString(),
                    modifier = Modifier,
                    style = TextStyle(
                        fontSize = 48.sp,
                        color = Color.Green
                    )
                )
                Text(text = mContext.getString(R.string.shelf_life_day))
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .border(2.dp, Color.Red, shape = RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = mContext.getString(R.string.expired),
                        modifier = Modifier.padding(4.dp),
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = (-remainingDays).toString(),
                    modifier = Modifier,
                    style = TextStyle(
                        fontSize = 48.sp,
                        color = Color.Red
                    )
                )

                Text(text = mContext.getString(R.string.shelf_life_day))
            }
        }
    }
}