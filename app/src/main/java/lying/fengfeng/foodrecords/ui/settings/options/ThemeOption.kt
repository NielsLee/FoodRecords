package lying.fengfeng.foodrecords.ui.settings.options

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.ui.LocalActivityContext
import lying.fengfeng.foodrecords.ui.theme.ThemeOptions

@Composable
fun ThemeOption(
    iconSize: Dp,
    titleFontSize: TextUnit,
    titlePadding: PaddingValues,
) {

    val activityContext = LocalActivityContext.current
    val appViewModel: FoodRecordsAppViewModel =
        viewModel(viewModelStoreOwner = (activityContext as MainActivity))

    var isNewUI by remember { appViewModel.isNewUI }
    var isNewUITried by remember { appViewModel.isNewUITried }

    var themeOptionExpanded by remember { mutableStateOf(false) }

    val bigIconSize = 30.dp

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
                imageVector = Icons.Filled.ColorLens,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(id = R.string.theme_option),
                fontSize = titleFontSize,
                modifier = Modifier.padding(paddingValues = titlePadding),
                color = MaterialTheme.colorScheme.onSurface
            )

            Badge(modifier = Modifier.padding(start = 8.dp), containerColor = if (isNewUITried) Color.Transparent else Color.Red)

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .rotate(if (themeOptionExpanded) 180f else 0f)
                    .clickable {
                        themeOptionExpanded = !themeOptionExpanded
                    },
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        AnimatedVisibility(visible = themeOptionExpanded) {
            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Spacer(modifier = Modifier.size(iconSize))
                    Text(text = stringResource(id = R.string.use_new_ui), color = MaterialTheme.colorScheme.onSurface)

                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isNewUI,
                        onCheckedChange = { isNewUiEnabled ->
                            appViewModel.setIsNewUI(isNewUiEnabled)
                            if (!isNewUiEnabled) {
                                appViewModel.setIsExtraColumnLayout(false) // new ui 横向布局太多，三行会有显示bug
                            }
                        },
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Spacer(modifier = Modifier.size(iconSize))
                    Text(text = stringResource(id = R.string.extra_column_layout), color = MaterialTheme.colorScheme.onSurface)
                    Badge(
                        modifier = Modifier.padding(start = 8.dp)
                    ) { Text(text = stringResource(id = R.string.badge_new)) }
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        enabled = appViewModel.isNewUI.value,
                        checked = appViewModel.isExtraColumnLayout.value,
                        onCheckedChange = { isExtraColumnEnabled ->
                            appViewModel.setIsExtraColumnLayout(isExtraColumnEnabled)
                            appViewModel.setIsNewUITried(true) // no need to switch back
                        },
                    )
                }

                Row {
                    LazyHorizontalStaggeredGrid(
                        rows = StaggeredGridCells.Fixed(1),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .heightIn(max = bigIconSize),
                        contentPadding = PaddingValues(horizontal = iconSize),
                        horizontalItemSpacing = 24.dp
                    ) {
                        items(
                            count = 7
                        ) { index ->
                            IconButton(
                                onClick = {
                                    appViewModel.setThemeOption(ThemeOptions.fromInt(index))
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        Brush.verticalGradient(
                                            Pair(0.1f, Color.Red),
                                            Pair(0.3f, Color.Yellow),
                                            Pair(0.6f, Color.Green),
                                            Pair(0.9f, Color.Blue)
                                        )
                                    )
                                    .size(bigIconSize)
                            ) {
                                Image(
                                    painter = when(index) {
                                        1 -> ColorPainter(Color.White)
                                        2 -> ColorPainter(Color.Yellow)
                                        3 -> ColorPainter(Color.Green)
                                        4 -> ColorPainter(Color.Blue)
                                        5 -> ColorPainter(Color.Magenta)
                                        6 -> ColorPainter(Color.Red)
                                        else -> ColorPainter(Color.Transparent)
                                    },
                                    contentDescription = null,
                                )
                                if (appViewModel.themeOption.value.int == index) {
                                    Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}