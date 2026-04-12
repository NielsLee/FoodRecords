package lying.fengfeng.foodrecords.ui.settings.options

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodTypeInfo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.LocalActivityContext

@Composable
fun FoodTypeOption(
    iconSize: Dp,
    titleFontSize: TextUnit,
    subTitleFontSize: TextUnit,
    snackBarHostState: SnackbarHostState
) {

    val context = LocalContext.current
    val activityContext = LocalActivityContext.current
    val appViewModel: FoodRecordsAppViewModel =
        viewModel(viewModelStoreOwner = (activityContext as MainActivity))
    val foodTypeList = remember { appViewModel.foodTypeList }
    var foodTypeOptionExpanded by remember { mutableStateOf(false) }
    var foodTypeDialogShown by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val focusRequester = remember { FocusRequester() }


    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(vertical = 8.dp)
            .scrollable(scrollState, Orientation.Vertical)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.food_drumstick_svg),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(id = R.string.food_type_option),
                fontSize = titleFontSize,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(visible = foodTypeOptionExpanded, enter = scaleIn()) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                        .clickable {
                            foodTypeDialogShown = true
                        },
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .rotate(if (foodTypeOptionExpanded) 180f else 0f)
                    .clickable {
                        foodTypeOptionExpanded = !foodTypeOptionExpanded
                    },
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        AnimatedVisibility(visible = foodTypeOptionExpanded) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyHorizontalStaggeredGrid(
                    rows = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.heightIn(max = 96.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(
                        key = { foodTypeList[it].type },
                        count = foodTypeList.size
                    ) { index ->
                        var selected by remember { mutableStateOf(false) }
                        val foodTypeInfo = foodTypeList[index]
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(2.dp)
                        ) {
                            InputChip(
                                selected = selected,
                                onClick = {
                                    selected = !selected
                                },
                                label = {
                                    Text(
                                        text = foodTypeInfo.type,
                                        fontSize = subTitleFontSize,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    if (selected) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = null,
                                            modifier = Modifier.clickable {
                                                appViewModel.removeFoodTypeInfo(foodTypeInfo)
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    val result = snackBarHostState.showSnackbar(
                                                        message = "${context.getString(R.string.removed)} ${foodTypeInfo.type}",
                                                        actionLabel = context.getString(R.string.undo),
                                                        duration = SnackbarDuration.Short
                                                    )
                                                    when (result) {
                                                        SnackbarResult.ActionPerformed -> {
                                                            appViewModel.addFoodTypeInfo(
                                                                foodTypeInfo
                                                            )
                                                        }

                                                        SnackbarResult.Dismissed -> {

                                                        }
                                                    }
                                                }
                                            },
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                border = null
                            )

                        }
                    }
                }
            }
        }
    }

    if (foodTypeDialogShown) {
        var input by remember { mutableStateOf("") }
        var isInputError by remember { mutableStateOf(false) }
        AlertDialog(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.food_drumstick_svg),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            },
            title = {
                Text(text = "${stringResource(id = R.string.add)} ${stringResource(id = R.string.title_type)}")
            },
            text = {
                OutlinedTextField(
                    value = input,
                    onValueChange = {
                        isInputError = it.isNotEmpty() && it.isBlank()
                        input = it
                    },
                    modifier = Modifier.focusRequester(focusRequester),
                    isError = isInputError
                )
            },
            onDismissRequest = {
                foodTypeDialogShown = false
            },
            confirmButton = {
                TextButton(
                    enabled = input.isNotEmpty() && !isInputError,
                    onClick = {
                        appViewModel.addFoodTypeInfo(FoodTypeInfo(type = input))
                        foodTypeDialogShown = false
                    }
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        foodTypeDialogShown = false
                    }
                ) {
                    Text(stringResource(id = R.string.dismiss))
                }
            }
        )
        LaunchedEffect(key1 = Unit) {
            focusRequester.requestFocus()
        }
    }

}