package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialogViewModel.CameraStatus

/**
 * 图片预览下方控制拍摄的按钮行
 */
@Composable
fun IconButtonRow(
    modifier: Modifier,
    cameraStatus: CameraStatus,
    onChecked: () -> Unit,
    onClosed: () -> Unit,
    onCameraCaptured: () -> Unit
) {
    val buttonsContracted = updateTransition(cameraStatus, label = "IconButtonsContracted")

    /* TODO 将dp改为相对位置 */
    val offsetXLeft by buttonsContracted.animateDp(
        label = "",
        transitionSpec = {
            if (targetState == CameraStatus.PREVIEWING) {
                keyframes {
                    durationMillis = 300
                    (-54).dp at 0
                    0.dp at 150

                }
            } else {
                keyframes {
                    durationMillis = 300
                    0.dp at 0
                    (-54).dp at 150
                }
            }
        }) { status ->
        if (status == CameraStatus.PREVIEWING) 0.dp else (-54).dp
    }

    /* TODO 将dp改为相对位置 */
    val offsetXRight by buttonsContracted.animateDp(
        label = "",
        transitionSpec = {
            if (targetState == CameraStatus.PREVIEWING) {
                keyframes {
                    durationMillis = 300
                    54.dp at 0
                    0.dp at 150
                }
            } else {
                keyframes {
                    durationMillis = 300
                    0.dp at 0
                    54.dp at 150
                }
            }
        }
    ) { status ->
        if (status == CameraStatus.PREVIEWING) 0.dp else 54.dp
    }

    val edgeButtonAlpha by buttonsContracted.animateFloat(
        transitionSpec = {
            if (targetState == CameraStatus.PREVIEWING) {
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
    ) { status ->
        if (status == CameraStatus.PREVIEWING) 0f else 1f
    }

    val centerButtonAlpha by buttonsContracted.animateFloat(
        transitionSpec = {
            if (targetState == CameraStatus.PREVIEWING) {
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
    ) { status ->
        if (status == CameraStatus.PREVIEWING) 1f else 0f
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
                onClick = onClosed,
                modifier = Modifier
                    .offset { IntOffset(offsetXLeft.roundToPx(), 0) }
                    .alpha(edgeButtonAlpha)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                enabled = (cameraStatus != CameraStatus.PREVIEWING)
            ) {
                Icon(Icons.Filled.Close, contentDescription = null)
            }

            IconButton(
                onClick = onChecked,
                modifier = Modifier
                    .offset { IntOffset(offsetXRight.roundToPx(), 0) }
                    .alpha(edgeButtonAlpha)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                enabled = (cameraStatus != CameraStatus.PREVIEWING)
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
            }

            IconButton(
                onClick = onCameraCaptured,
                modifier = Modifier
                    .alpha(centerButtonAlpha)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                enabled = (cameraStatus == CameraStatus.PREVIEWING)
            ) {
                Icon(Icons.Filled.Camera, contentDescription = null)
            }
        }
    }
}
