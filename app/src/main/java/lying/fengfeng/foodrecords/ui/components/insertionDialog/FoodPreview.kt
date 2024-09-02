package lying.fengfeng.foodrecords.ui.components.insertionDialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.CamSelector
import com.ujizin.camposer.state.CameraState
import com.ujizin.camposer.state.ImageTargetSize
import com.ujizin.camposer.state.rememberCamSelector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.ui.components.insertionDialog.InsertionDialogViewModel.CameraStatus
import java.io.File

/**
 * 图片预览窗
 */
@Composable
fun FoodPreview(
    uuid: String,
    context: Context,
    cameraState: CameraState,
    mutableCameraStatus: MutableState<CameraStatus>
) {
    val camSelector by rememberCamSelector(CamSelector.Back)
    var cameraPermissionGranted by remember { mutableStateOf(checkCameraPermission(context)) }

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
                                val permission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                                if (permission == PackageManager.PERMISSION_GRANTED) {
                                    cameraPermissionGranted = true
                                    mutableCameraStatus.value = CameraStatus.PREVIEWING
                                } else {
                                    ActivityCompat.requestPermissions(
                                        context as Activity,
                                        arrayOf(Manifest.permission.CAMERA),
                                        888
                                    )
                                }
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
                var bitmap by remember { mutableStateOf(createPreviewBitmap()) }
                val imageBitmap = bitmap.asImageBitmap()
                val painter = BitmapPainter(imageBitmap)

                LaunchedEffect(Unit) {
                    CoroutineScope(Dispatchers.IO).launch {
                        // make sure the picture file is not empty
                        if (File(picturePath).exists() && File(picturePath).length() > 100) {
                            bitmap = Glide.with(context).asBitmap()
                                .load(picturePath)
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .skipMemoryCache(true)
                                .submit().get()
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

private fun checkCameraPermission(context: Context): Boolean {
    val permission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    )
    return permission == PackageManager.PERMISSION_GRANTED
}
