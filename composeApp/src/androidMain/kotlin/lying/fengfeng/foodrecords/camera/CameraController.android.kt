package lying.fengfeng.foodrecords.camera

import com.ujizin.camposer.state.CameraState
import lying.fengfeng.foodrecords.MainActivityDelegate
import java.io.File

actual class CameraController {

    val cameraState: CameraState = CameraState(MainActivityDelegate.context)

    actual fun takePicture(filePath: String, onResult: () -> Unit) {
        val file = File(filePath)
        cameraState.takePicture(file) {
            onResult.invoke()
            //cameraStatus = CameraStatus.IMAGE_READY
        }
    }

}