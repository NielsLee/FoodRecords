package lying.fengfeng.foodrecords.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.ext.scaleToHalfScreenWidth
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.LocalScreenParams
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialog
import lying.fengfeng.foodrecords.utils.DateUtil
import lying.fengfeng.foodrecords.utils.EffectUtil
import lying.fengfeng.foodrecords.utils.ImageUtil

@Composable
fun FoodInfoCardNew(
    foodInfo: FoodInfo,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val screenParams = LocalScreenParams.current
    val appViewModel: FoodRecordsAppViewModel =
        viewModel(viewModelStoreOwner = (context as MainActivity))
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    val foodPicturePath = AppRepo.getPicturePath(foodInfo.uuid)
    var imageBitmap by remember {
        mutableStateOf(
            ImageUtil.preProcessImage(foodPicturePath)?.let {
                it.scaleToHalfScreenWidth(context)?.asImageBitmap()
            }
        )
    }

    var tipsShown by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }
    var isAmountAnimating by remember { mutableStateOf(false) }

    val iconButtonSize = 24.dp
    val numberFontSize = 24.sp

    val (remainingDays, isExpired) = DateUtil.getRemainingDays(foodInfo)
    val expiredColor = if (isExpired) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val expiredTextColor = if (isExpired) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary

    Card(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth(1f / screenParams.listColumnNum),
        shape = RoundedCornerShape(12.dp)
    ) {

        val foodNameScrollState = rememberScrollState()
        val expirationDateScrollState = rememberScrollState()

        // inner card for padding
        Card(
            modifier = Modifier.padding(8.dp)
        ) {
            // Title Row
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = iconButtonSize)
                ) {
                    if (foodInfo.tips.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                tipsShown = !tipsShown
                            },
                            modifier = Modifier
                                .size(iconButtonSize)
                        ) {
                            Icon(imageVector = Icons.Filled.Info, contentDescription = null)
                        }
                    }

                    Text(
                        text = foodInfo.foodName,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(2.dp)
                            .horizontalScroll(foodNameScrollState)
                    )
                }
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    IconButton(
                        onClick = {
                            dropDownMenuExpanded = true
                            EffectUtil.playVibrationEffect(context)
                        },
                        modifier = Modifier
                            .size(iconButtonSize)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreHoriz,
                            contentDescription = null,
                            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                        )
                    }

                    DropdownMenu(
                        expanded = dropDownMenuExpanded,
                        onDismissRequest = { dropDownMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.eat_svg),
                                    null,
                                )
                            },
                            text = {
                                Text(text = context.getString(R.string.eat))
                            },
                            onClick = {
                                EffectUtil.playVibrationEffect(context)
                                dropDownMenuExpanded = false
                                coroutineScope.launch(Dispatchers.IO) {
                                    if (foodInfo.amount > 1) {
                                        foodInfo.amount -= 1
                                        appViewModel.updateFoodInfo(foodInfo)
                                        launch(Dispatchers.Main) {
                                            isAmountAnimating = true
                                        }
                                    } else {
                                        deleteFood(appViewModel, foodInfo)
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
                                Text(text = context.getString(R.string.supplement))
                            },
                            onClick = {
                                EffectUtil.playVibrationEffect(context)
                                dropDownMenuExpanded = false
                                coroutineScope.launch(Dispatchers.IO) {
                                    foodInfo.amount += 1
                                    appViewModel.updateFoodInfo(foodInfo)
                                    launch(Dispatchers.Main) {
                                        isAmountAnimating = true
                                    }
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
                                Text(text = context.getString(R.string.edit))
                            },
                            onClick = {
                                EffectUtil.playVibrationEffect(context)
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
                                Text(text = context.getString(R.string.delete_record))
                            },
                            onClick = {
                                EffectUtil.playVibrationEffect(context)
                                dropDownMenuExpanded = false
                                coroutineScope.launch(Dispatchers.IO) {
                                    deleteFood(appViewModel, foodInfo)
                                }
                            }
                        )
                    }
                }
            }

            // content row
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
            ) {
                // for picture, shelf life, amount
                Card(
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        imageBitmap?.let {
                            Image(
                                bitmap = it,
                                contentDescription = null,
                                modifier = Modifier
                            )
                        }

                        // for shelf life and amount
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.secondary.copy(0.7f),
                                            MaterialTheme.colorScheme.secondary.copy(0.9f),
                                            MaterialTheme.colorScheme.secondary.copy(1f)
                                        ),
                                    )
                                )
                        ) {
                            Row {
                                // for shelf life
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp, top = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Text(
                                            text = remainingDays.let {
                                                if (it > 99) {
                                                    "99+"
                                                } else {
                                                    it.toString()
                                                }
                                            },
                                            color = if (isExpired) expiredColor else expiredTextColor,
                                            fontSize = numberFontSize
                                        )
                                        Text(
                                            text = stringResource(id = R.string.shelf_life_day),
                                            fontSize = 12.sp,
                                            color = if (isExpired) expiredColor else expiredTextColor,
                                        )
                                    }

                                    // 三行的情况下数字标签可能会换行 所以只在两行的情况下显示
                                    if (screenParams.listColumnNum == 2) {
                                        Text(
                                            text =
                                            if (isExpired) stringResource(id = R.string.expired)
                                            else stringResource(id = R.string.valid_in),
                                            color = expiredTextColor,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(
                                                    if (isExpired) MaterialTheme.colorScheme.error
                                                    else MaterialTheme.colorScheme.primary
                                                )
                                                .padding(0.dp) //outer padding
                                                .padding(horizontal = 8.dp),
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                // for amount
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(end = 8.dp, bottom = 4.dp, top = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        val shakeAnimation = animateFloatAsState(
                                            targetValue = if (isAmountAnimating) 1f else 0f,
                                            animationSpec = keyframes {
                                                0f at 0
                                                -5f at 100
                                                5f at 200
                                                -5f at 300
                                                5f at 400
                                                -5f at 500
                                                5f at 600
                                                -5f at 700
                                                5f at 800
                                                0f at 1000
                                            },
                                            finishedListener = { 
                                                if (isAmountAnimating) {
                                                    isAmountAnimating = false
                                                }
                                            }
                                        )
                                        Row(
                                            verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                text = "x",
                                                fontSize = 12.sp,
                                                color = if (isExpired) expiredColor else expiredTextColor,
                                                )
                                        }
                                            Text(
                                                text = foodInfo.amount.toString(),
                                                modifier = Modifier.offset(x = shakeAnimation.value.dp),
                                                color = if (isExpired) expiredColor else expiredTextColor,
                                                fontSize = numberFontSize
                                            )
                                        }
                                    // 三行的情况下数字标签可能会换行 所以只在两行的情况下显示
                                    if (screenParams.listColumnNum == 2) {
                                        Text(
                                            text = stringResource(id = R.string.amount),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(MaterialTheme.colorScheme.primary)
                                                .padding(0.dp) //outer padding
                                                .padding(horizontal = 8.dp), // inner padding
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = if (isExpired) Icons.Filled.DeleteForever else Icons.Filled.AccessAlarm,
                        modifier = Modifier.size(18.dp),
                        contentDescription = "Expiration date icon",
                        tint = expiredTextColor
                    )
                    Text(
                        text = DateUtil.getExpirationDate(foodInfo),
                        fontSize = 15.sp,
                        modifier = Modifier.horizontalScroll(expirationDateScrollState),
                        color = expiredTextColor
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    // for tips
    AnimatedVisibility(
        visible = tipsShown,
        enter = slideInVertically(
            initialOffsetY = { it }
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { -it }
        ) + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .animateContentSize()
                .padding(4.dp)
                .clickable { tipsShown = false },
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = foodInfo.tips,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }

    // for editing dialog
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