package lying.fengfeng.foodrecords.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lying.fengfeng.foodrecords.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        // TODO chip的增删和持久化

        val iconSize = 36.dp
        val subIconSize = 24.dp
        val titlePadding = PaddingValues(start = 8.dp)

        var foodTypeOptionExpanded by remember { mutableStateOf(false) }
        var shelfOptionExpanded by remember { mutableStateOf(false) }
        var notificationOptionExpanded by remember { mutableStateOf(false) }
        var notificationEnabled by remember { mutableStateOf(false) }
        var infoExpanded by remember { mutableStateOf(false) }

        val list = (0..20).toList()

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.food_drumstick_svg),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Text(text = stringResource(id = R.string.food_type_option), fontSize = 24.sp, modifier = Modifier.padding(start = 8.dp))

                Spacer(modifier = Modifier.weight(1f))


                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .rotate(if (foodTypeOptionExpanded) 180f else 0f)
                        .clickable {
                            foodTypeOptionExpanded = !foodTypeOptionExpanded
                        }
                )
            }

            AnimatedVisibility(visible = foodTypeOptionExpanded) {
                Row {
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(2),
                        modifier = Modifier.heightIn(max = 96.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(
                            count = list.size
                        ) { it ->
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(2.dp)
                            ) {
                                InputChip(
                                    selected = false,
                                    onClick = { },
                                    label = { Text(text = "这是Chip $it", fontSize = 18.sp) },
                                    colors = InputChipDefaults.inputChipColors(
                                        containerColor = Color.Cyan
                                    ),
                                    border = null
                                )

                            }
                        }
                    }
                }
            }
        }


        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_range_svg),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
                Text(
                    text = stringResource(id = R.string.shelf_life_option),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(paddingValues = titlePadding)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                        .rotate(if (shelfOptionExpanded) 180f else 0f)
                        .clickable {
                            shelfOptionExpanded = !shelfOptionExpanded
                        }
                )
            }

            AnimatedVisibility(visible = shelfOptionExpanded) {
                Row {
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(2),
                        modifier = Modifier.heightIn(max = 96.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(
                            count = list.size
                        ) { it ->
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(2.dp)
                            ) {
                                InputChip(
                                    selected = false,
                                    onClick = { },
                                    label = { Text(text = "这是Chip $it", fontSize = 18.sp) },
                                    colors = InputChipDefaults.inputChipColors(
                                        containerColor = Color.Cyan
                                    ),
                                    border = null
                                )

                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )

                Text(
                    text = stringResource(id = R.string.notification_option),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(paddingValues = titlePadding)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                        .rotate(if (notificationOptionExpanded) 180f else 0f)
                        .clickable {
                            notificationOptionExpanded = !notificationOptionExpanded
                        }
                )
            }

            AnimatedVisibility(visible = notificationOptionExpanded) {
                Column {

                    Row(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Spacer(modifier = Modifier.size(iconSize))
                        Text(
                            text = stringResource(id = R.string.enable_notification),
                            fontSize = 24.sp,
                            modifier = Modifier.padding(paddingValues = titlePadding)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Checkbox(
                            checked = notificationEnabled,
                            onCheckedChange = { notificationEnabled = !notificationEnabled },
                            modifier = Modifier.size(iconSize)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Spacer(modifier = Modifier.size(iconSize))
                        Text(
                            text = stringResource(id = R.string.days_of_advance_notice),
                            fontSize = 24.sp,
                            modifier = Modifier.padding(paddingValues = titlePadding)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        NumberPickerWithButtons()
                    }
                }

            }
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )

                Text(
                    text = stringResource(id = R.string.about_app),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(paddingValues = titlePadding)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                        .rotate(if (infoExpanded) 180f else 0f)
                        .clickable {
                            infoExpanded = !infoExpanded
                        }
                )
            }

            AnimatedVisibility(visible = infoExpanded) {
                Column {
                    Row(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Spacer(modifier = Modifier.size(iconSize))

                        Text(
                            text = stringResource(id = R.string.source_code_repo),
                            fontSize = 24.sp,
                            modifier = Modifier.padding(paddingValues = titlePadding)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                // TODO Github链接
                            },
                            modifier = Modifier.size(iconSize)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.github_mark_svg),
                                contentDescription = null
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Spacer(modifier = Modifier.size(iconSize))
                        Text(
                            text = stringResource(id = R.string.contact_author),
                            fontSize = 24.sp,
                            modifier = Modifier.padding(paddingValues = titlePadding)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                // TODO 微信信息
                            },
                            modifier = Modifier.size(iconSize)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.wechat_svg),
                                contentDescription = null
                            )
                        }


                        Spacer(modifier = Modifier.padding(horizontal = 6.dp))

                        IconButton(
                            onClick = {
                                // TODO 邮件跳转
                            },
                            modifier = Modifier.size(iconSize)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Mail,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberPickerWithButtons(
    initialNumber: Int = 0,
    minNumber: Int = Int.MIN_VALUE,
    maxNumber: Int = Int.MAX_VALUE,
    onNumberChange: (Int) -> Unit = {}
) {
    var number by remember { mutableStateOf(initialNumber) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
    ) {
        IconButton(
            onClick = {
                if (number > minNumber) {
                    number--
                    onNumberChange(number)
                }
            },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(imageVector = Icons.Filled.Remove, contentDescription = null)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = number.toString(),
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            onClick = {
                if (number < maxNumber) {
                    number++
                    onNumberChange(number)
                }
            },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}