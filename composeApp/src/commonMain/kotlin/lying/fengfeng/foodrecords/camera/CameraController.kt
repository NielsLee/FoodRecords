package lying.fengfeng.foodrecords.camera

expect class CameraController() {

    fun takePicture(filePath: String, onResult: () -> Unit)

}