package lying.fengfeng.foodrecords.ui.components.insertionDialog

import android.util.Log
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
import com.ujizin.camposer.state.CameraState
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.repository.FoodInfoRepo
import java.io.File

/**
 * 图片预览下方控制拍摄的按钮行
 */
@Composable
fun IconButtonRow(
    modifier: Modifier,
    cameraState: CameraState,
) {

    val dialogViewModel: InsertionDialogViewModel = viewModel()

    var showDialog by remember { dialogViewModel.isDialogShown }
    var cameraStatus by remember { dialogViewModel.cameraStatus }

    val buttonsContracted = updateTransition(cameraStatus, label = "IconButtonsContracted")
    val pictureUUID = LocalUUID.current

    val offsetXLeft by buttonsContracted.animateDp(
        label = "",
        transitionSpec = {
            if (targetState == InsertionDialogViewModel.CameraStatus.PREVIEWING) {
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
        if (status == InsertionDialogViewModel.CameraStatus.PREVIEWING) 0.dp else (-54).dp
    }

    val offsetXRight by buttonsContracted.animateDp(
        label = "",
        transitionSpec = {
            if (targetState == InsertionDialogViewModel.CameraStatus.PREVIEWING) {
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
        if (status == InsertionDialogViewModel.CameraStatus.PREVIEWING) 0.dp else 54.dp
    }

    val edgeButtonAlpha by buttonsContracted.animateFloat(
        transitionSpec = {
            if (targetState == InsertionDialogViewModel.CameraStatus.PREVIEWING) {
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
        if (status == InsertionDialogViewModel.CameraStatus.PREVIEWING) 0f else 1f
    }

    val centerButtonAlpha by buttonsContracted.animateFloat(
        transitionSpec = {
            if (targetState == InsertionDialogViewModel.CameraStatus.PREVIEWING) {
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
        if (status == InsertionDialogViewModel.CameraStatus.PREVIEWING) 1f else 0f
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
                    dialogViewModel.initParams()
                },
                modifier = Modifier
                    .offset { IntOffset(offsetXLeft.roundToPx(), 0) }
                    .alpha(edgeButtonAlpha)
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
                enabled = (cameraStatus != InsertionDialogViewModel.CameraStatus.PREVIEWING)
            ) {
                Icon(Icons.Filled.Close, contentDescription = null)
            }

            IconButton(
                onClick = {
                    val foodInfo = FoodInfo(
                        dialogViewModel.foodName.value,
                        dialogViewModel.productionDate.value,
                        dialogViewModel.foodType.value,
                        dialogViewModel.shelfLife.value,
                        pictureUUID
                    )
                    Log.d("LLF", "IconButtonRow: $foodInfo")
                    FoodInfoRepo.insert(foodInfo)
                    showDialog = false
                    dialogViewModel.initParams()
                },
                modifier = Modifier
                    .offset { IntOffset(offsetXRight.roundToPx(), 0) }
                    .alpha(edgeButtonAlpha)
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
                enabled = (cameraStatus != InsertionDialogViewModel.CameraStatus.PREVIEWING)
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
            }

            IconButton(
                onClick = {
                    val file = File(FoodInfoRepo.getPicturePath(pictureUUID))
                    dialogViewModel.uuid.value = pictureUUID
                    cameraState.takePicture(file) {
                        Log.d("LLF", "IconButtonRow: $it, file = ${file.path}")
                        cameraStatus = InsertionDialogViewModel.CameraStatus.IMAGE_READY
                    }
                },
                modifier = Modifier
                    .alpha(centerButtonAlpha)
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
                enabled = (cameraStatus == InsertionDialogViewModel.CameraStatus.PREVIEWING)
            ) {
                Icon(Icons.Filled.Camera, contentDescription = null)
            }
        }
    }
}
