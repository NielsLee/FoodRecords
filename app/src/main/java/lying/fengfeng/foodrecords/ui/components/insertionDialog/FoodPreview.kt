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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
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
import lying.fengfeng.foodrecords.repository.FoodInfoRepo

@Composable
fun FoodPreview(
    context: Context,
    cameraState: CameraState
) {
    val dialogViewModel: InsertionDialogViewModel = viewModel()
    val camSelector by rememberCamSelector(CamSelector.Back)
    var cameraPermissionGranted by remember { mutableStateOf(false) }
    val isCaptured by remember { dialogViewModel.isCaptured }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (cameraPermissionGranted) {
            if (!isCaptured) {
                CameraPreview(
                    cameraState = cameraState,
                    camSelector = camSelector,
                    modifier = Modifier.fillMaxSize(),
                    isFocusOnTapEnabled = false,
                    imageCaptureTargetSize = ImageTargetSize(
                        outputSize = CameraController.OutputSize(Size(480, 640)))
                ) {

                }
            } else {
                val picturePath = FoodInfoRepo.getAbsolutePictureDir() + dialogViewModel.pictureUUID.value
                var bitmap by remember { mutableStateOf(createBitmap()) }
                val imageBitmap = bitmap.asImageBitmap()
                val painter = BitmapPainter(imageBitmap)

                LaunchedEffect(Unit) {
                    CoroutineScope(Dispatchers.IO).launch {
                        bitmap = Glide.with(context).asBitmap().load(picturePath)
                            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                            .submit().get()
                    }
                }

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            IconButton(
                onClick = {
                    MainScope().launch {
                        val permission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        )
                        if (permission == PackageManager.PERMISSION_GRANTED) {
                            cameraPermissionGranted = true
                            dialogViewModel.isPreviewing.value = true
                        } else {
                            ActivityCompat.requestPermissions(
                                context as Activity,
                                arrayOf(Manifest.permission.CAMERA),
                                888
                            )
                        }
                    }
                },
                Modifier.fillMaxSize()
            ) {
                Icon(Icons.Filled.CameraAlt, null)
            }
        }
    }
}

fun createBitmap(): Bitmap {

    val bitmap = Bitmap.createBitmap(300, 400, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = android.graphics.Paint().apply {
        color = Color.argb(127, Black.toArgb(), Black.toArgb(), Black.toArgb())
    }
    canvas.drawRect(0f, 0f, 300f, 400f, paint)

    return bitmap
}
