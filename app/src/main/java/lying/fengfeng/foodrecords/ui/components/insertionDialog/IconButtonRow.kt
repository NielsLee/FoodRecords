package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun IconButtonRow(
    modifier: Modifier
) {
    val dialogViewModel: InsertionDialogViewModel = viewModel()

    var isPreviewing by remember { dialogViewModel.isPreviewing }
    var showDialog by remember { dialogViewModel.isDialogShown }

    val buttonsExpanded = updateTransition(!isPreviewing, label = "IconButtonsTransition")

    val offsetXLeft by buttonsExpanded.animateDp(
        label = "",
        transitionSpec = {
            if (targetState) {
                keyframes {
                    durationMillis = 300
                    0.dp at 0
                    (-54).dp at 150
                }
            } else {
                keyframes {
                    durationMillis = 300
                    (-54).dp at 0
                    0.dp at 150
                }
            }
    }) { expanded ->
        if (expanded) (-54).dp else 0.dp
    }

    val offsetXRight by buttonsExpanded.animateDp(
        label = "",
        transitionSpec = {
            if (targetState) {
                keyframes {
                    durationMillis = 300
                    0.dp at 0
                    54.dp at 150
                }
            } else {
                keyframes {
                    durationMillis = 300
                    54.dp at 0
                    0.dp at 150
                }
            }
        }
    ) { expanded ->
        if (expanded) 54.dp else 0.dp
    }

    val edgeButtonAlpha by buttonsExpanded.animateFloat(
        transitionSpec = {
            if (targetState) {
                keyframes {
                    durationMillis = 300
                    0f at 0
                    1f at 300
                }
            } else {
                keyframes {
                    durationMillis = 300
                    1f at 0
                    0f at 300
                }
            }
        }, label = ""
    ) {
        if (it) 1f else 0f
    }

    val centerButtonAlpha by buttonsExpanded.animateFloat(
        transitionSpec = {
            if (targetState) {
                keyframes {
                    durationMillis = 300
                    1f at 0
                    0f at 300
                }
            } else {
                keyframes {
                    durationMillis = 300
                    0f at 0
                    1f at 300
                }
            }
        }, label = ""
    ) {
        if (it) 0f else 1f
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {

            IconButton(
                onClick = {
                    showDialog = false
                },
                modifier = Modifier
                    .offset { IntOffset(offsetXLeft.roundToPx(), 0) }
                    .alpha(edgeButtonAlpha)
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
                enabled = !isPreviewing
            ) {
                Icon(Icons.Filled.Close, contentDescription = null)
            }

            IconButton(
                onClick = {

                },
                modifier = Modifier
                    .offset { IntOffset(offsetXRight.roundToPx(), 0) }
                    .alpha(edgeButtonAlpha)
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
                enabled = !isPreviewing
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
            }

            IconButton(
                onClick = {
                    //TODO: take a photo
                    isPreviewing = false
                },
                modifier = Modifier
                    .alpha(centerButtonAlpha)
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
                enabled = isPreviewing
            ) {
                Icon(Icons.Filled.Camera, contentDescription = null)
            }
        }
    }
}
