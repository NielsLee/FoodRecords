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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.utils.DateUtil.dateWithFormat
import lying.fengfeng.foodrecords.utils.DateUtil.todayMillis

object TempData {

    val foodTypes = listOf("CXK", "FCC", "CLN", "MJQ")
    var shelfLifeList = listOf(
        "1Day",
        "1Day",
        "1Day",
        "7Days",
        "15Days",
        "30Days",
        "60Days",
        "90Days",
        "180Days"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertionDialog() {

    val configuration = LocalConfiguration.current
    val mContext = LocalContext.current

    val dialogViewModel: InsertionDialogViewModel = viewModel()
    var isDialogShown by remember { dialogViewModel.isDialogShown }
    var isPreviewing by remember { dialogViewModel.isPreviewing }

    var isLandScape by remember { mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) }

    var typeSelectionExpanded by remember { mutableStateOf(false) }
    var selectedTypeText by remember { mutableStateOf(TempData.foodTypes[0]) }

    var shelfLifeExpanded by remember { mutableStateOf(false) }
    var shelfLifeValue by remember { mutableStateOf(TempData.shelfLifeList[0]) }

    var foodName by remember { mutableStateOf("FoodName") }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = todayMillis())
    var openDialog by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(dateWithFormat(todayMillis(), "YYYY-MM-dd")) }

    val focusRequester = remember { FocusRequester() }

    Dialog(
        onDismissRequest = {
            isDialogShown = false
            isPreviewing = false
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
                text = "Add new food!",
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
                            label = { Text("Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged {
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
                            value = date,
                            onValueChange = { newValue ->
                                date = newValue
                            },
                            label = { Text("Select Date") },
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
                                                date = dateWithFormat(it, "YYYY-MM-dd")
                                            }
                                        },
                                    ) {
                                        Text("OK")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            openDialog = false
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                },
                                properties = DialogProperties(
                                    dismissOnClickOutside = false
                                ),
                            ) {

                                DatePicker(state = datePickerState)
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
                            value = selectedTypeText,
                            onValueChange = { },
                            label = { Text("Type") },
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

                            TempData.foodTypes.forEach { selectionOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedTypeText = selectionOption
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
                            value = shelfLifeValue,
                            onValueChange = { },
                            label = { Text("Shelf Life") },
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

                            TempData.shelfLifeList.forEach { selectionOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        shelfLifeValue = selectionOption
                                        shelfLifeExpanded = false
                                    },
                                    text = {
                                        Text(text = selectionOption)
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
                ) {

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                    ) {
                        FoodPreview(mContext)
                    }

//                    Row(
//                        modifier = Modifier.fillMaxHeight(),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        IconButton(
//                            onClick = { /*TODO*/ },
//                            modifier = Modifier
//                                .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
//                        ) {
//                            Icon(
//                                Icons.Filled.Close,
//                                contentDescription = null,
//                            )
//                        }
//
//                        Spacer(Modifier.weight(1f))
//
//                        IconButton(
//                            onClick = { /*TODO*/ },
//                            modifier = Modifier
//                                .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
//                        ) {
//                            Icon(
//                                Icons.Filled.Check,
//                                contentDescription = null,
//                            )
//                        }
//                    }

                    IconButtonRow(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}