package lying.fengfeng.foodrecords.ui.components.insertionDialog

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Size
import androidx.camera.view.CameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.CamSelector
import com.ujizin.camposer.state.CameraState
import com.ujizin.camposer.state.ImageTargetSize
import com.ujizin.camposer.state.rememberCamSelector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialogViewModel.CameraStatus
import lying.fengfeng.foodrecords.utils.CameraUtil
import lying.fengfeng.foodrecords.utils.FileUtil
import lying.fengfeng.foodrecords.utils.ImageUtil

/**
 * 图片预览窗
 */
@Composable
fun FoodPreview(
    uuid: String,
    cameraState: CameraState,
    mutableCameraStatus: MutableState<CameraStatus>
) {
    val camSelector by rememberCamSelector(CamSelector.Back)
    var cameraPermissionGranted by remember { mutableStateOf(CameraUtil.checkCameraPermission()) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when(mutableCameraStatus.value) {
            CameraStatus.IDLE -> {
                IconButton(
                    onClick = {
                        if (cameraPermissionGranted) {
                            mutableCameraStatus.value = CameraStatus.PREVIEWING
                        } else {
                            MainScope().launch {
                                cameraPermissionGranted = CameraUtil.checkCameraPermission()
                                if (cameraPermissionGranted) {
                                    mutableCameraStatus.value = CameraStatus.PREVIEWING
                                }

                                CameraUtil.requestCameraPermission()
                            }
                        }
                    },
                    Modifier.fillMaxSize()
                ) {
                    Icon(Icons.Filled.CameraAlt, null)
                }
            }
            CameraStatus.PREVIEWING -> {

                CameraPreview(
                    cameraState = cameraState,
                    camSelector = camSelector,
                    modifier = Modifier.fillMaxSize(),
                    isFocusOnTapEnabled = false,
                    imageCaptureTargetSize = ImageTargetSize(
                        outputSize = CameraController.OutputSize(Size(480, 640)))
                ) {

                }
            }
            CameraStatus.IMAGE_READY -> {
                val picturePath = AppRepo.getPicturePath(uuid)
                var imageBitmap by remember { mutableStateOf(createPreviewBitmap().asImageBitmap()) }
                val painter = BitmapPainter(imageBitmap)

                LaunchedEffect(Unit) {
                    CoroutineScope(Dispatchers.IO).launch {
                        // make sure the picture file is not empty
                        if (FileUtil.isFileExist(picturePath)) {
                            imageBitmap = ImageUtil.getImageBitmapFromFile(picturePath)
                        } else {
                            mutableCameraStatus.value = CameraStatus.IDLE
                        }
                    }
                }

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

private fun createPreviewBitmap(): Bitmap {

    val bitmap = Bitmap.createBitmap(1200, 1600, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = android.graphics.Paint().apply {
        color = Color.argb(0, 0, 0, 0)
    }
    canvas.drawRect(0f, 0f, 1200f, 1600f, paint)

    return bitmap
}
