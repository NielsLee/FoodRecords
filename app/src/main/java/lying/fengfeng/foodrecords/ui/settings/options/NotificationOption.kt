package lying.fengfeng.foodrecords.ui.settings.options

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.LocalActivityContext
import lying.fengfeng.foodrecords.ui.components.NumberPickerWithButtons

@Composable
fun NotificationOption(
    iconSize: Dp,
    titleFontSize: TextUnit,
    titlePadding: PaddingValues,
) {

    val activityContext = LocalActivityContext.current
    val appViewModel: FoodRecordsAppViewModel =
        viewModel(viewModelStoreOwner = (activityContext as MainActivity))

    var notificationOptionExpanded by remember { mutableStateOf(false) }
    var notificationEnabled by remember { appViewModel.isNotificationEnabled }

    val daysBeforeNotification by remember { appViewModel.daysBeforeNotification }

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
                imageVector = Icons.Filled.Notifications,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(id = R.string.notification_option),
                fontSize = titleFontSize,
                modifier = Modifier.padding(paddingValues = titlePadding),
                color = MaterialTheme.colorScheme.onSurface
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
                    },
                tint = MaterialTheme.colorScheme.onSurface
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
                        modifier = Modifier.padding(paddingValues = titlePadding),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Checkbox(
                        checked = notificationEnabled,
                        onCheckedChange = {
                            notificationEnabled = !notificationEnabled
                            if (notificationEnabled) {
                                appViewModel.enableNotification(activityContext.applicationContext)
                            } else {
                                appViewModel.disableNotification(activityContext.applicationContext)
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
                        modifier = Modifier.padding(paddingValues = titlePadding),
                        color = MaterialTheme.colorScheme.onSurface
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
}