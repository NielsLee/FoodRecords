package lying.fengfeng.foodrecords.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ChipColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.TextUnit
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
        // TODO 文字资源
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
                Text(text = "食物种类", fontSize = 24.sp, modifier = Modifier.padding(start = 8.dp))

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
                    text = "保质期选项",
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
                    text = "提醒设置",
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
                            text = "开启提醒",
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
                            text = "提醒提前天数：",
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
                    text = "关于应用",
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
                            text = "代码仓库",
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
                            text = "联系作者",
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
            // TODO 提醒设置也改为下拉菜单
        }
    }
}