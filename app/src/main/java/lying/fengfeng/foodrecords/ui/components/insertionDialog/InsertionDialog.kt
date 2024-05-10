package lying.fengfeng.foodrecords.ui.components.insertionDialog

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ujizin.camposer.state.rememberCameraState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.utils.DateUtil.dateWithFormat
import lying.fengfeng.foodrecords.utils.DateUtil.todayMillis
import java.util.UUID

/**
 * 添加食物记录的弹窗
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertionDialog() {

    val pictureUUID = UUID.randomUUID().toString()

    val configuration = LocalConfiguration.current
    val mContext = LocalContext.current

    val dialogViewModel: InsertionDialogViewModel = viewModel()
    var isDialogShown by remember { dialogViewModel.isDialogShown }
    var cameraStatus by remember { dialogViewModel.cameraStatus }

    var isLandScape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }

    var typeSelectionExpanded by remember { mutableStateOf(false) }

    var shelfLifeExpanded by remember { mutableStateOf(false) }

    var foodName by remember { dialogViewModel.foodName }
    var productionDate by remember { dialogViewModel.productionDate }
    var foodType by remember { dialogViewModel.foodType }
    var shelfLife by remember { dialogViewModel.shelfLife }

    val datePickerState = rememberDatePickerState(todayMillis())
    var openDialog by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val cameraState = rememberCameraState()

    cameraStatus = InsertionDialogViewModel.CameraStatus.IDLE

    CompositionLocalProvider(LocalUUID provides pictureUUID) {

        Dialog(
            onDismissRequest = {
                isDialogShown = false
                cameraStatus = InsertionDialogViewModel.CameraStatus.IDLE
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = false
            ),
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .padding(12.dp)
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .aspectRatio(
                        1f / 1f
                    )

            ) {
                Text(
                    text = mContext.getString(R.string.title_add_new),
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
                                .weight(1f)
                        ) {
                            OutlinedTextField(
                                value = foodName,
                                onValueChange = { newText ->
                                    foodName = newText
                                },
                                label = { Text(text = mContext.getString(R.string.title_name)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        foodName = ""
                                    },
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                            )
                        }

                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }

                        // 生产日期
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                readOnly = false,
                                value = productionDate,
                                onValueChange = { newValue ->
                                    productionDate = newValue
                                },
                                label = { Text(text = mContext.getString(R.string.title_production_date)) },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        MainScope().launch {
                                            openDialog = true
                                        }
                                    }) {
                                        Icon(Icons.Filled.DateRange, null)
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
                                                    productionDate =
                                                        dateWithFormat(it, "YYYY-MM-dd")
                                                }
                                            },
                                        ) {
                                            Text(mContext.getString(R.string.ok))
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                openDialog = false
                                            }
                                        ) {
                                            Text(mContext.getString(R.string.cancel))
                                        }
                                    },
                                    properties = DialogProperties(
                                        dismissOnClickOutside = false
                                    ),
                                ) {

                                    DatePicker(
                                        state = datePickerState,
                                        dateValidator = {
                                            it <= todayMillis()
                                        }
                                    )
                                }
                            }

                        }

                        // 分类
                        ExposedDropdownMenuBox(
                            expanded = typeSelectionExpanded,
                            onExpandedChange = { typeSelectionExpanded = !typeSelectionExpanded },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)

                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = foodType,
                                onValueChange = { },
                                label = { Text(text = mContext.getString(R.string.title_type)) },
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

                                InsertionDialogViewModel.TempData.foodTypes.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        onClick = {
                                            foodType = selectionOption
                                            typeSelectionExpanded = false
                                        },
                                        text = {
                                            Text(text = selectionOption)
                                        }
                                    )
                                }
                            }
                        }

                        // 保质期
                        ExposedDropdownMenuBox(
                            expanded = shelfLifeExpanded,
                            onExpandedChange = { shelfLifeExpanded = !shelfLifeExpanded },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = "$shelfLife ${mContext.getString(R.string.shelf_life_day)}",
                                onValueChange = { },
                                label = { Text(text = mContext.getString(R.string.title_shelf_life)) },
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

                                InsertionDialogViewModel.TempData.shelfLifeList.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        onClick = {
                                            shelfLife = selectionOption
                                            shelfLifeExpanded = false
                                        },
                                        text = {
                                            Text(text = "$selectionOption ${mContext.getString(R.string.shelf_life_day)}")
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
                            .padding(top = 8.dp)
                    ) {

                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(3f / 4f)
                        ) {
                            FoodPreview(mContext, cameraState)
                        }

                        IconButtonRow(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 4.dp),
                            cameraState,
                        )
                    }
                }
            }
        }
    }
}


val LocalUUID = compositionLocalOf<String> {
    error("No UUID provided")
}