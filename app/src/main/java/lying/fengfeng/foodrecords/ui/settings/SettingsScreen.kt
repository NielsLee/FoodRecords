package lying.fengfeng.foodrecords.ui.settings

import StatisticPermissionState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.ui.settings.options.AboutAppOption
import lying.fengfeng.foodrecords.ui.settings.options.DateFormatOption
import lying.fengfeng.foodrecords.ui.settings.options.FoodTypeOption
import lying.fengfeng.foodrecords.ui.settings.options.NotificationOption
import lying.fengfeng.foodrecords.ui.settings.options.ShelfLifeOption
import lying.fengfeng.foodrecords.ui.settings.options.ThemeOption

@Composable
fun SettingsScreen(
    snackBarHostState: SnackbarHostState
) {

    val scrollState = rememberScrollState()
    val phoneHomePermissionState by remember {
        mutableIntStateOf(AppRepo.getPhoneHomePermissionState())
    }

    var showPhoneHomeDialog by remember {
        mutableStateOf(false)
    }

    if (phoneHomePermissionState == 0) {
        LaunchedEffect(phoneHomePermissionState) {
            delay(1000) // delay 1s
            showPhoneHomeDialog = true
        }
    }

    if (showPhoneHomeDialog) {
        AlertDialog(onDismissRequest = {
            AppRepo.setPhoneHomePermissionState(StatisticPermissionState.DENIED.ordinal) // no
            showPhoneHomeDialog = false
        }, confirmButton = {
            Button(onClick = {
                AppRepo.setPhoneHomePermissionState(StatisticPermissionState.ALLOWED.ordinal) // yes!!!
                showPhoneHomeDialog = false
            }) {
                Text(text = stringResource(id = R.string.allow))
            }
        },
            dismissButton = {
                Button(onClick = {
                    AppRepo.setPhoneHomePermissionState(StatisticPermissionState.DENIED.ordinal) // no
                    showPhoneHomeDialog = false
                }) {
                    Text(text = stringResource(id = R.string.deny))
                }
            },
            title = {
                Text(text = stringResource(id = R.string.diagnostic_data_dialog_title))
            },
            text = {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.diagnostic_data_permission_question) + "\n")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(id = R.string.diagnostic_data_includes))
                        }
                        append(stringResource(id = R.string.diagnostic_data_includes_app_stats))
                        append(stringResource(id = R.string.diagnostic_data_includes_crash_logs))
                        append("\n")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(id = R.string.diagnostic_data_excludes))
                        }
                        append(stringResource(id = R.string.diagnostic_data_excludes_user_data))
                    }
                )
            }

        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(scrollState)
    ) {

        val iconSize = 24.dp
        val bigIconSize = 30.dp
        val titleFontSize = 14.sp
        val subTitleFontSize = 12.sp
        val titlePadding = PaddingValues(start = 8.dp)

        // 种类设置
        FoodTypeOption(
            iconSize = iconSize,
            titleFontSize = titleFontSize,
            subTitleFontSize = subTitleFontSize,
            snackBarHostState = snackBarHostState
        )

        // 保质期设置
        ShelfLifeOption(
            iconSize = iconSize,
            titleFontSize = titleFontSize,
            titlePadding = titlePadding,
            subTitleFontSize = subTitleFontSize,
            snackBarHostState = snackBarHostState
        )

        // 日期格式设置
        DateFormatOption(
            iconSize = iconSize,
            titleFontSize = titleFontSize,
            subTitleFontSize = subTitleFontSize
        )

        // 提醒设置
        NotificationOption(
            iconSize = iconSize,
            titleFontSize = titleFontSize,
            titlePadding = titlePadding
        )

        // 主题设置
        ThemeOption(
            iconSize = iconSize,
            titleFontSize = titleFontSize,
            titlePadding = titlePadding
        )

        // 关于应用
        AboutAppOption(
            iconSize = iconSize,
            titleFontSize = titleFontSize,
            titlePadding = titlePadding
        )
    }
}