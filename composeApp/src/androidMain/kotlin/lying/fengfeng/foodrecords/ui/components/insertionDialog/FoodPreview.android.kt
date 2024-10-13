package lying.fengfeng.foodrecords.ui.components.insertionDialog

import android.util.Size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ujizin.camposer.state.CamSelector
import com.ujizin.camposer.state.ImageTargetSize
import lying.fengfeng.foodrecords.camera.CameraController

@Composable
actual fun CameraPreview(cameraController: CameraController) {
    com.ujizin.camposer.CameraPreview(
        cameraState = cameraController.cameraState,
        camSelector = CamSelector.Back,
        modifier = Modifier.fillMaxSize(),
        isFocusOnTapEnabled = false,
        imageCaptureTargetSize = ImageTargetSize(
            outputSize = androidx.camera.view.CameraController.OutputSize(Size(480, 640))
        )
    ) {

    }
}