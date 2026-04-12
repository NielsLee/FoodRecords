package lying.fengfeng.foodrecords.ui.settings.options

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.LocalActivityContext
import java.util.Locale

@Composable
fun DateFormatOption(
    iconSize: Dp,
    titleFontSize: TextUnit,
    subTitleFontSize: TextUnit
) {
    val context = LocalContext.current
    val activityContext = LocalActivityContext.current
    val appViewModel: FoodRecordsAppViewModel =
        viewModel(viewModelStoreOwner = (activityContext as MainActivity))

    val dateFormatGridState = rememberLazyStaggeredGridState()

    var dateFormatOptionExpanded by remember { mutableStateOf(false) }

    val dateFormatList = listOf(
        "yy-MM-dd",
        "dd-MM-yy",
        "MM-dd-yy",
        "yyyy-MM-dd",
        "dd-MM-yyyy",
        "MM-dd-yyyy"
    )


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
                imageVector = Icons.Filled.Numbers,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(id = R.string.date_format_option),
                fontSize = titleFontSize,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .rotate(if (dateFormatOptionExpanded) 180f else 0f)
                    .clickable {
                        dateFormatOptionExpanded = !dateFormatOptionExpanded
                    },
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        AnimatedVisibility(visible = dateFormatOptionExpanded) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                var currentDateFormat by remember { appViewModel.dateFormat }

                LazyHorizontalStaggeredGrid(
                    rows = StaggeredGridCells.Fixed(1),
                    modifier = Modifier.heightIn(max = 48.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    state = dateFormatGridState
                ) {
                    items(
                        count = dateFormatList.size,
                        key = { index -> dateFormatList[index] }
                    ) { index ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = currentDateFormat == dateFormatList[index],
                                onCheckedChange = {
                                    currentDateFormat = dateFormatList[index]
                                    appViewModel.updateDateFormat(currentDateFormat)
                                }
                            )
                            Text(
                                text = dateFormatList[index].uppercase(Locale.getDefault()),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(8.dp),
                                fontSize = subTitleFontSize,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                LaunchedEffect(key1 = Unit) {
                    dateFormatList.forEachIndexed { index, dateFormat ->
                        if (currentDateFormat == dateFormat) {
                            dateFormatGridState.animateScrollToItem(index)
                        }
                    }
                }
            }
        }
    }
}