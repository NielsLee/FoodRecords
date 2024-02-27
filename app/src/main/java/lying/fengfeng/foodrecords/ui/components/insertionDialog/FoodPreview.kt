package lying.fengfeng.foodrecords.ui.components.insertionDialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.CamSelector
import com.ujizin.camposer.state.CameraState
import com.ujizin.camposer.state.ImageTargetSize
import com.ujizin.camposer.state.rememberCamSelector
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.repository.FoodInfoRepo
import lying.fengfeng.foodrecords.utils.ImageUtil

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
                val bitmap = BitmapFactory.decodeFile(picturePath)
                val rotate = ImageUtil.getImageOrientation(picturePath)
                val imageBitmap = bitmap.asImageBitmap()
                val painter = BitmapPainter(imageBitmap)

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().rotate(rotate.toFloat())
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
