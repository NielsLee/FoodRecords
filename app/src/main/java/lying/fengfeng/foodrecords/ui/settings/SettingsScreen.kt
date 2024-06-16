package lying.fengfeng.foodrecords.ui.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodTypeInfo
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.LocalActivityContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    snackBarHostState: SnackbarHostState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {

        val context = LocalContext.current
        val activityContext = LocalActivityContext.current
        val appViewModel: FoodRecordsAppViewModel =
            viewModel(viewModelStoreOwner = (activityContext as MainActivity))

        val iconSize = 36.dp
        val subIconSize = 24.dp
        val titleFontSize = 18.sp
        val titlePadding = PaddingValues(start = 8.dp)

        var foodTypeOptionExpanded by remember { mutableStateOf(false) }
        var shelfOptionExpanded by remember { mutableStateOf(false) }
        var notificationOptionExpanded by remember { mutableStateOf(false) }
        var infoExpanded by remember { mutableStateOf(false) }
        var wechatInfoExpanded by remember { mutableStateOf(false) }

        var foodTypeDialogShown by remember { mutableStateOf(false) }
        var shelfLifeDialogShown by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }

        val foodTypeList = remember { appViewModel.foodTypeList }
        val shelfLifeList = remember { appViewModel.shelfLifeList }
        var notificationEnabled by remember { appViewModel.isNotificationEnabled }
        var daysBeforeNotification by remember { appViewModel.daysBeforeNotification }

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
                    modifier = Modifier.size(iconSize)
                )
                Text(
                    text = stringResource(id = R.string.food_type_option),
                    fontSize = titleFontSize,
                    modifier = Modifier.padding(start = 8.dp)
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
                            }
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
                            shelfOptionExpanded = false
                            notificationOptionExpanded = false
                            infoExpanded = false
                            wechatInfoExpanded = false
                        }
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
                                            fontSize = 18.sp
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
                                                }
                                            )
                                        }
                                    },
                                    colors = InputChipDefaults.inputChipColors(
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
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
                    fontSize = titleFontSize,
                    modifier = Modifier.padding(paddingValues = titlePadding)
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
                            }
                    )
                }

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                        .rotate(if (shelfOptionExpanded) 180f else 0f)
                        .clickable {
                            foodTypeOptionExpanded = false
                            shelfOptionExpanded = !shelfOptionExpanded
                            notificationOptionExpanded = false
                            infoExpanded = false
                            wechatInfoExpanded = false
                        }
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
                                            fontSize = 18.sp
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
                                                }
                                            )
                                        }
                                    },
                                    onClick = { selected = !selected },
                                    colors = InputChipDefaults.inputChipColors(
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                    ),
                                    border = null,
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
                    fontSize = titleFontSize,
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
                            foodTypeOptionExpanded = false
                            shelfOptionExpanded = false
                            notificationOptionExpanded = !notificationOptionExpanded
                            infoExpanded = false
                            wechatInfoExpanded = false
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
                            fontSize = titleFontSize,
                            modifier = Modifier.padding(paddingValues = titlePadding)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Checkbox(
                            checked = notificationEnabled,
                            onCheckedChange = {
                                notificationEnabled = !notificationEnabled
                                if (notificationEnabled) {
                                    appViewModel.enableNotification(activityContext)
                                } else {
                                    appViewModel.disableNotification(activityContext)
                                }
                            },
                            modifier = Modifier.size(iconSize)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Spacer(modifier = Modifier.size(iconSize))
                        Text(
                            text = stringResource(id = R.string.days_of_advance_notice),
                            fontSize = titleFontSize,
                            modifier = Modifier.padding(paddingValues = titlePadding)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        NumberPickerWithButtons(
                            initialNumber = daysBeforeNotification,
                            onNumberChange = {
                                appViewModel.updateDaysBeforeNotification(it)
                            }
                        )
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
                    fontSize = titleFontSize,
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
                            foodTypeOptionExpanded = false
                            shelfOptionExpanded = false
                            notificationOptionExpanded = false
                            infoExpanded = !infoExpanded
                            wechatInfoExpanded = false
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
                            fontSize = titleFontSize,
                            modifier = Modifier.padding(paddingValues = titlePadding)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier.size(iconSize)
                        ) {
                            GitHubButton()
                        }
                    }

                    Row(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Spacer(modifier = Modifier.size(iconSize))
                        Text(
                            text = stringResource(id = R.string.contact_author),
                            fontSize = titleFontSize,
                            modifier = Modifier.padding(paddingValues = titlePadding)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                wechatInfoExpanded = !wechatInfoExpanded
                            },
                            modifier = Modifier.size(iconSize)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.wechat_svg),
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.padding(horizontal = 6.dp))

                        Box(
                            modifier = Modifier.size(iconSize)
                        ) {
                            EmailButton()
                        }
                    }
                }
            }
        }

        AnimatedVisibility(visible = wechatInfoExpanded) {
            Card(
                modifier = Modifier.fillMaxSize(0.9f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_wechat),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "(✿´‿`)")
                            Text(text = "添加请注明来意哦")
                        }
                    }
                }
            }
        }

        if (foodTypeDialogShown) {
            var input by remember { mutableStateOf("") }
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
                        onValueChange = { input = it },
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                },
                onDismissRequest = {
                    foodTypeDialogShown = false
                },
                confirmButton = {
                    TextButton(
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
                            appViewModel.addShelfLifeInfo(ShelfLifeInfo(life = input))
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

@Composable
fun NumberPickerWithButtons(
    initialNumber: Int = 0,
    minNumber: Int = 0,
    maxNumber: Int = Int.MAX_VALUE,
    expanded: Boolean = false,
    onNumberChange: (Int) -> Unit = {}
) {
    var number by remember { mutableIntStateOf(initialNumber) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
    ) {
        if (expanded) {
            IconButton(
                onClick = {
                    number -= 20
                    if (number <= minNumber) {
                        number = 0
                        onNumberChange(number)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Remove,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        IconButton(
            onClick = {
                if (number > minNumber) {
                    number--
                }
                onNumberChange(number)
            },
            modifier = Modifier.size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
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
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }

        if (expanded) {
            IconButton(
                onClick = {
                    number += 20
                    if (number >= maxNumber) {
                        number = maxNumber
                    }
                    onNumberChange(number)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun EmailButton() {

    val context = LocalContext.current

    IconButton(onClick = {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("Niels_Lee@outlook.com"))
        }

        if (emailIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(emailIntent)
            Toast.makeText(
                context,
                context.getString(R.string.thanks_when_email),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, context.getString(R.string.no_email_app), Toast.LENGTH_SHORT)
                .show()
        }
    }
    ) {
        Icon(
            imageVector = Icons.Filled.Mail,
            contentDescription = null,
        )
    }
}

@Composable
fun GitHubButton() {

    val context = LocalContext.current

    IconButton(
        onClick = {
            val githubPackageName = "com.github.android"
            val githubRepoUrl = "https://github.com/NielsLee/FoodRecords"
            val githubAppUri = Uri.parse("github://github.com/NielsLee/FoodRecords")
            val webUri = Uri.parse(githubRepoUrl)

            val githubIntent = Intent(Intent.ACTION_VIEW, githubAppUri).apply {
                setPackage(githubPackageName)
            }

            val webIntent = Intent(Intent.ACTION_VIEW, webUri)

            if (isAppInstalled(context.packageManager, githubPackageName)) {
                try {
                    context.startActivity(githubIntent)
                } catch (e: Exception) {
                    context.startActivity(webIntent)
                }
            } else {
                context.startActivity(webIntent)
            }
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.github_mark_svg),
            contentDescription = null
        )
    }
}

fun isAppInstalled(packageManager: PackageManager, packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}