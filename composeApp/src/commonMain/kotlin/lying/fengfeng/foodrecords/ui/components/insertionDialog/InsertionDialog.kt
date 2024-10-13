package lying.fengfeng.foodrecords.ui.components.insertionDialog

import AppRepo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import fridgey_kmf.composeapp.generated.resources.Res
import fridgey_kmf.composeapp.generated.resources.cancel
import fridgey_kmf.composeapp.generated.resources.edit
import fridgey_kmf.composeapp.generated.resources.ok
import fridgey_kmf.composeapp.generated.resources.shelf_life_day
import fridgey_kmf.composeapp.generated.resources.tips_title
import fridgey_kmf.composeapp.generated.resources.title_add_new
import fridgey_kmf.composeapp.generated.resources.title_expiration_date
import fridgey_kmf.composeapp.generated.resources.title_name
import fridgey_kmf.composeapp.generated.resources.title_production_date
import fridgey_kmf.composeapp.generated.resources.title_shelf_life
import fridgey_kmf.composeapp.generated.resources.title_type
import fridgey_kmf.composeapp.generated.resources.toast_enter_name
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.camera.CameraController
import lying.fengfeng.foodrecords.entity.FoodInfo
import lying.fengfeng.foodrecords.entity.FoodTypeInfo
import lying.fengfeng.foodrecords.entity.ShelfLifeInfo
import lying.fengfeng.foodrecords.ui.LocalScreenParams
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialogViewModel.CameraStatus
import lying.fengfeng.foodrecords.ui.settings.NumberPickerWithButtons
import lying.fengfeng.foodrecords.utils.DateUtil
import lying.fengfeng.foodrecords.utils.DateUtil.dateWithFormat
import lying.fengfeng.foodrecords.utils.DateUtil.todayMillis
import lying.fengfeng.foodrecords.utils.EffectUtil
import lying.fengfeng.foodrecords.utils.ToastUtil
import org.jetbrains.compose.resources.stringResource

/**
 * 添加食物记录的弹窗
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertionDialog(
    shelfLifeList: List<ShelfLifeInfo>,
    foodTypeList: List<FoodTypeInfo>,
    onDismiss: () -> Unit,
    onFoodInfoCreated: (FoodInfo) -> Unit,
    existedFoodInfo: FoodInfo? = null
) {
    // viewModel's lifecycle is the same as this dialog
    // fill existed params or create a set of new params
    val dialogViewModel = InsertionDialogViewModel(existedFoodInfo)
    var cameraStatus by remember { dialogViewModel.cameraStatus }

    var isExpireDate by remember { mutableStateOf(false) }

    var typeSelectionExpanded by remember { mutableStateOf(false) }

    var shelfLifeExpanded by remember { mutableStateOf(false) }

    val uuid by remember { dialogViewModel.uuid }
    var foodName by remember { dialogViewModel.foodName }
    var productionDate by remember { dialogViewModel.productionDate }
    var expirationDate by remember { dialogViewModel.expirationDate }
    var foodType by remember { dialogViewModel.foodType }
    var shelfLife by remember { dialogViewModel.shelfLife }
    var amount by remember { dialogViewModel.amount }
    var tips by remember { dialogViewModel.tips }

    val datePickerState = rememberDatePickerState(todayMillis())
    var openDialog by remember { mutableStateOf(false) }
    var tipsInputShown by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val cameraController = CameraController()
    val mutableCameraStatus = dialogViewModel.cameraStatus
    CompositionLocalProvider(LocalUUID provides uuid) {

        Dialog(
            onDismissRequest = {
                cameraStatus = CameraStatus.IDLE
                onDismiss.invoke()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = false
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(LocalScreenParams.current.getInsertDialogWidthPercent())
                        .aspectRatio(
                            1f / 1.15f
                        )

                ) {
                    Text(
                        text = if (existedFoodInfo == null)
                            stringResource(resource = Res.string.title_add_new)
                        else
                            stringResource(resource = Res.string.edit),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 24.dp, top = 10.dp),
                        style = TextStyle(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Row {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.Start
                        ) {

                            // 名称
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                OutlinedTextField(
                                    value = foodName,
                                    onValueChange = { newText ->
                                        foodName = newText
                                    },
                                    maxLines = 1,
                                    label = { Text(text = stringResource(resource = Res.string.title_name)) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(focusRequester),
                                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                                )
                            }

                            LaunchedEffect(Unit) {
                                focusRequester.requestFocus()
                            }


                            Column(
                                modifier = Modifier.weight(0.6f)
                            ) {
                                // 切换生产日期和保质期
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                ) {

                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = stringResource(resource = Res.string.title_production_date),
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    Switch(
                                        checked = isExpireDate,
                                        onCheckedChange = { isExpireDate = it },
                                        thumbContent = {
                                            Icon(
                                                imageVector = if (isExpireDate) Icons.Filled.DeleteForever else Icons.Filled.CalendarMonth,
                                                contentDescription = null,
                                                modifier = Modifier.size(SwitchDefaults.IconSize)
                                            )
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                                            checkedTrackColor = Color.Transparent,
                                            checkedBorderColor = MaterialTheme.colorScheme.primary,
                                            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                                        )
                                    )

                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = stringResource(resource = Res.string.title_expiration_date),
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                }
                            }


                            // 生产日期
                            Box(
                                modifier = Modifier
                                    .weight(1f),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                OutlinedTextField(
                                    readOnly = true,
                                    value = if (isExpireDate) expirationDate else productionDate,
                                    maxLines = 1,
                                    onValueChange = { newValue ->
                                        if (isExpireDate) {
                                            expirationDate = newValue
                                        } else {
                                            productionDate = newValue
                                        }
                                    },
                                    label = {
                                        Icon(
                                            imageVector = if (isExpireDate) {
                                                Icons.Filled.DeleteForever
                                            } else {
                                                Icons.Filled.CalendarMonth
                                            }, contentDescription = null
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            MainScope().launch {
                                                openDialog = true
                                            }
                                        }) {
                                            Icon(Icons.Filled.EditCalendar, null)
                                        }
                                    },
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )

                                if (openDialog) {
                                    DatePickerDialog(
                                        onDismissRequest = {
                                            openDialog = false
                                        },
                                        confirmButton = {
                                            TextButton(
                                                onClick = {
                                                    openDialog = false
                                                    datePickerState.selectedDateMillis?.also {
                                                        if (isExpireDate) {
                                                            expirationDate =
                                                                dateWithFormat(
                                                                    it,
                                                                    AppRepo.getDateFormat()
                                                                )
                                                        } else {
                                                            productionDate =
                                                                dateWithFormat(
                                                                    it,
                                                                    AppRepo.getDateFormat()
                                                                )
                                                        }
                                                    }
                                                },
                                            ) {
                                                Text(stringResource(resource = Res.string.ok))
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(
                                                onClick = {
                                                    openDialog = false
                                                }
                                            ) {
                                                Text(stringResource(resource = Res.string.cancel))
                                            }
                                        },
                                        properties = DialogProperties(
                                            dismissOnClickOutside = false
                                        ),
                                    ) {

                                        DatePicker(
                                            state = datePickerState
                                        )
                                    }
                                }

                            }

                            // 保质期
                            ExposedDropdownMenuBox(
                                expanded = !isExpireDate && shelfLifeExpanded,
                                onExpandedChange = {
                                    if (!isExpireDate) shelfLifeExpanded = !shelfLifeExpanded
                                },
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                OutlinedTextField(
                                    enabled = !isExpireDate,
                                    readOnly = true,
                                    value = if (isExpireDate) {
                                        "####"
                                    } else {
                                        "$shelfLife ${stringResource(resource = Res.string.shelf_life_day)}"
                                    },
                                    maxLines = 1,
                                    onValueChange = { },
                                    label = { Text(text = stringResource(resource = Res.string.title_shelf_life)) },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = shelfLifeExpanded
                                        )
                                    },
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                        .clickable(enabled = false) {
                                        }
                                )

                                DropdownMenu(
                                    expanded = shelfLifeExpanded,
                                    onDismissRequest = { shelfLifeExpanded = false },
                                    modifier = Modifier.exposedDropdownSize(),
                                ) {

                                    shelfLifeList.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            onClick = {
                                                shelfLife = selectionOption.life
                                                shelfLifeExpanded = false
                                            },
                                            text = {
                                                Text(
                                                    text = "${selectionOption.life} ${
                                                        stringResource(resource = Res.string.shelf_life_day)
                                                    }"
                                                )
                                            }
                                        )
                                    }
                                }
                            }

                            // 分类
                            ExposedDropdownMenuBox(
                                expanded = typeSelectionExpanded,
                                onExpandedChange = {
                                    typeSelectionExpanded = !typeSelectionExpanded
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                OutlinedTextField(
                                    readOnly = true,
                                    value = foodType,
                                    maxLines = 1,
                                    onValueChange = { },
                                    label = { Text(text = stringResource(resource = Res.string.title_type)) },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = typeSelectionExpanded
                                        )
                                    },
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                        .clickable(enabled = false) {
                                        }
                                )


                                DropdownMenu(
                                    expanded = typeSelectionExpanded,
                                    onDismissRequest = { typeSelectionExpanded = false },
                                    modifier = Modifier.exposedDropdownSize()

                                ) {

                                    foodTypeList.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            onClick = {
                                                foodType = selectionOption.type
                                                typeSelectionExpanded = false
                                            },
                                            text = {
                                                Text(text = selectionOption.type)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .padding(top = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val toastContent = stringResource(resource = Res.string.toast_enter_name)

                            OutlinedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(3f / 4f)
                            ) {
                                FoodPreview(uuid, cameraController, mutableCameraStatus)
                            }

                            NumberPickerWithButtons(
                                initialNumber = amount,
                                minNumber = 1,
                                modifier = Modifier.padding(2.dp),
                                onNumberChange = { newNumber ->
                                    amount = newNumber
                                }
                            )

                            IconButtonRow(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 4.dp),
                                cameraStatus = cameraStatus,
                                onChecked = {
                                    if (foodName.isEmpty()) {
                                        ToastUtil.show(toastContent)
                                        return@IconButtonRow
                                    }
                                    val foodInfo = FoodInfo(
                                        foodName = foodName,
                                        productionDate = DateUtil.validateDateFormat(productionDate),
                                        foodType = foodType,
                                        shelfLife = shelfLife,
                                        expirationDate = DateUtil.validateDateFormat(expirationDate),
                                        uuid = uuid,
                                        amount = amount,
                                        tips = tips
                                    )
                                    EffectUtil.playVibrationEffect()
                                    onDismiss.invoke()
                                    onFoodInfoCreated.invoke(foodInfo)
                                },
                                onClosed = {
                                    EffectUtil.playVibrationEffect()
                                    onDismiss.invoke()
                                },
                                onCameraCaptured = {
                                    EffectUtil.playVibrationEffect()
                                    cameraController.takePicture(AppRepo.getPicturePath(uuid)) {
                                        cameraStatus = CameraStatus.IMAGE_READY
                                    }
                                    dialogViewModel.uuid.value = uuid
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            tipsInputShown = true
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = if (tips.isEmpty()) Icons.Filled.Edit else Icons.Filled.EditNote,
                            contentDescription = null
                        )
                    }
                }

                AnimatedVisibility(visible = tipsInputShown) {
                    AlertDialog(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null
                            )
                        },
                        title = {
                            Text(text = stringResource(resource = Res.string.tips_title))
                        },
                        text = {
                            OutlinedTextField(
                                value = tips,
                                onValueChange = { tips = it },
                                modifier = Modifier.focusRequester(focusRequester)
                            )
                        },
                        onDismissRequest = {
                            tipsInputShown = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    tipsInputShown = false
                                }
                            ) {
                                Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    tips = ""
                                    tipsInputShown = false
                                }
                            ) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                            }
                        }
                    )

                    LaunchedEffect(key1 = Unit) {
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    }
}


val LocalUUID = compositionLocalOf<String> {
    error("No UUID provided")
}