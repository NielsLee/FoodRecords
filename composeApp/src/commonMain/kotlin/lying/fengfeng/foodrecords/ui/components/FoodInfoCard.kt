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
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fridgey_kmf.composeapp.generated.resources.Res
import fridgey_kmf.composeapp.generated.resources.delete_record
import fridgey_kmf.composeapp.generated.resources.eat
import fridgey_kmf.composeapp.generated.resources.eat_svg
import fridgey_kmf.composeapp.generated.resources.edit
import fridgey_kmf.composeapp.generated.resources.expired
import fridgey_kmf.composeapp.generated.resources.shelf_life_day
import fridgey_kmf.composeapp.generated.resources.supplement
import fridgey_kmf.composeapp.generated.resources.valid_in
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.entity.FoodInfo
import lying.fengfeng.foodrecords.ui.AppViewModelFactory
import lying.fengfeng.foodrecords.ui.AppViewModelOwner
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialog
import lying.fengfeng.foodrecords.ui.theme.ExpiredGreen
import lying.fengfeng.foodrecords.ui.theme.ExpiredRed
import lying.fengfeng.foodrecords.utils.DateUtil
import lying.fengfeng.foodrecords.utils.EffectUtil
import lying.fengfeng.foodrecords.utils.FileUtil
import lying.fengfeng.foodrecords.utils.ImageUtil
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun FoodInfoCard(
    foodInfo: FoodInfo,
    modifier: Modifier = Modifier
) {

    val appViewModel: FoodRecordsAppViewModel =
        viewModel(viewModelStoreOwner = AppViewModelOwner, factory = AppViewModelFactory())
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val tipsButtonShown by remember { mutableStateOf(foodInfo.tips.isNotEmpty()) }
    var tipsShown by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }


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
                        .padding(4.dp)
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
                    LaunchedEffect(isEditing) {
                        val bitmap = ImageUtil.preProcessImage(foodPicturePath)
                        // 切换回主线程更新UI
                        launch(Dispatchers.Main) {
                            imageBitmap = bitmap
                        }
                    }
                }

                RemainingDaysWindow(
                    foodInfo = foodInfo,
                    isHorizontal = imageBitmap == null
                )
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {

            Column(
                Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(0.7f)
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
                        EffectUtil.playVibrationEffect()
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
                        leadingIcon = {
                            Icon(
                                painter = painterResource(resource = Res.drawable.eat_svg),
                                null,
                            )
                        },
                        text = {
                            Text(stringResource(resource = Res.string.eat))
                        },
                        onClick = {
                            EffectUtil.playVibrationEffect()
                            dropDownMenuExpanded = false
                            coroutineScope.launch(Dispatchers.IO) {
                                if (foodInfo.amount > 1) {
                                    foodInfo.amount -= 1
                                    appViewModel.updateFoodInfo(foodInfo)
                                } else {
                                    FileUtil.deleteFood(appViewModel, foodInfo)
                                }
                            }
                        }
                    )

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.AddShoppingCart,
                                null,
                            )
                        },
                        text = {
                            Text(stringResource(resource = Res.string.supplement))
                        },
                        onClick = {
                            EffectUtil.playVibrationEffect()
                            dropDownMenuExpanded = false
                            coroutineScope.launch(Dispatchers.IO) {
                                foodInfo.amount += 1
                                appViewModel.updateFoodInfo(foodInfo)
                            }
                        }
                    )

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                null,
                            )
                        },
                        text = {
                            Text(stringResource(resource = Res.string.edit))
                        },
                        onClick = {
                            EffectUtil.playVibrationEffect()
                            isEditing = true
                            dropDownMenuExpanded = false
                        }
                    )

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                null,
                                modifier = Modifier.padding(1.dp)
                            )
                        },
                        text = {
                            Text(stringResource(resource = Res.string.delete_record))
                        },
                        onClick = {
                            EffectUtil.playVibrationEffect()
                            dropDownMenuExpanded = false
                            coroutineScope.launch(Dispatchers.IO) {
                                FileUtil.deleteFood(appViewModel, foodInfo)
                            }
                        }
                    )
                }
            }
        }

        if (isEditing) {
            InsertionDialog(
                shelfLifeList = appViewModel.shelfLifeList,
                foodTypeList = appViewModel.foodTypeList,
                onDismiss = {
                    isEditing = false
                },
                onFoodInfoCreated = {
                    appViewModel.addOrUpdateFoodInfo(it)
                },
                existedFoodInfo = foodInfo
            )
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
    foodInfo: FoodInfo,
    isHorizontal: Boolean = false
) {
    val (remainingDays, isExpired) = DateUtil.getRemainingDays(foodInfo)
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val fontSize = 32.sp

        val (remainingTitleColor, remainingTitleText) = if (!isExpired) {
            ExpiredGreen to stringResource(resource = Res.string.valid_in)
        } else {
            ExpiredRed to stringResource(resource = Res.string.expired)
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
                    if (it > 99) {
                        "99+"
                    } else {
                        it.toString()
                    }
                },
                modifier = Modifier,
                style = TextStyle(
                    fontSize = fontSize,
                    color = remainingTitleColor
                )
            )

            Text(stringResource(resource = Res.string.shelf_life_day))
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