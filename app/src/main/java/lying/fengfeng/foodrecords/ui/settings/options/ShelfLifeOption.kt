package lying.fengfeng.foodrecords.ui.settings.options

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.LocalActivityContext
import lying.fengfeng.foodrecords.ui.components.NumberPickerWithButtons

@Composable
fun ShelfLifeOption(
    iconSize: Dp,
    titleFontSize: TextUnit,
    titlePadding: PaddingValues,
    subTitleFontSize: TextUnit,
    snackBarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(vertical = 8.dp),
    ) {

        val context = LocalContext.current
        val activityContext = LocalActivityContext.current
        val appViewModel: FoodRecordsAppViewModel =
            viewModel(viewModelStoreOwner = (activityContext as MainActivity))

        var shelfOptionExpanded by remember { mutableStateOf(false) }
        var shelfLifeDialogShown by remember { mutableStateOf(false) }
        val shelfLifeList = remember { appViewModel.shelfLifeList }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar_range_svg),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(id = R.string.shelf_life_option),
                fontSize = titleFontSize,
                modifier = Modifier.padding(paddingValues = titlePadding),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(visible = shelfOptionExpanded, enter = scaleIn()) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                        .clickable {
                            shelfLifeDialogShown = true
                        },
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .rotate(if (shelfOptionExpanded) 180f else 0f)
                    .clickable {
                        shelfOptionExpanded = !shelfOptionExpanded
                    },
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        AnimatedVisibility(visible = shelfOptionExpanded) {
            Row {
                LazyHorizontalStaggeredGrid(
                    rows = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.heightIn(max = 96.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(
                        key = { shelfLifeList[it].life },
                        count = shelfLifeList.size
                    ) { index ->
                        var selected by remember { mutableStateOf(false) }
                        val shelfLifeInfo = shelfLifeList[index]
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(2.dp)
                        ) {
                            InputChip(
                                selected = selected,
                                label = {
                                    Text(
                                        text = shelfLifeInfo.life + stringResource(id = R.string.shelf_life_day),
                                        fontSize = subTitleFontSize,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    if (selected) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = null,
                                            modifier = Modifier.clickable {
                                                appViewModel.removeShelfLifeInfo(shelfLifeInfo)
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    val result = snackBarHostState.showSnackbar(
                                                        message = "${context.getString(R.string.removed)} ${shelfLifeInfo.life}${
                                                            context.getString(
                                                                R.string.shelf_life_day
                                                            )
                                                        }",
                                                        actionLabel = context.getString(R.string.undo),
                                                        duration = SnackbarDuration.Short
                                                    )
                                                    when (result) {
                                                        SnackbarResult.ActionPerformed -> {
                                                            appViewModel.addShelfLifeInfo(
                                                                shelfLifeInfo
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
                                onClick = { selected = !selected },
                                colors = InputChipDefaults.inputChipColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                border = null,
                            )

                        }
                    }
                }
            }
        }

        if (shelfLifeDialogShown) {
            var input by remember { mutableStateOf("") }
            AlertDialog(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_range_svg),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                },
                title = {
                    Text(text = "${stringResource(id = R.string.add)} ${stringResource(id = R.string.title_shelf_life)}")
                },
                text = {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        NumberPickerWithButtons(
                            expanded = true,
                            onNumberChange = {
                                input = it.toString()
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                },
                onDismissRequest = {
                    shelfLifeDialogShown = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (input.isNotEmpty()) {
                                appViewModel.addShelfLifeInfo(ShelfLifeInfo(life = input))
                            }
                            shelfLifeDialogShown = false
                        }
                    ) {
                        Text(stringResource(id = R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            shelfLifeDialogShown = false
                        }
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                }
            )
        }

    }
}