package lying.fengfeng.foodrecords.ui.components.insertionDialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.CamSelector
import com.ujizin.camposer.state.rememberCamSelector
import com.ujizin.camposer.state.rememberCameraState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun FoodPreview(
    mContext: Context
) {
    val dialogViewModel: InsertionDialogViewModel = viewModel()
    val cameraState = rememberCameraState()
    val camSelector by rememberCamSelector(CamSelector.Back)
    var cameraPermissionGranted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (cameraPermissionGranted) {
            CameraPreview(
                cameraState = cameraState,
                camSelector = camSelector,
                modifier = Modifier.fillMaxSize(),
                isFocusOnTapEnabled = false
            ) {

            }
        } else {
            IconButton(
                onClick = {
                    MainScope().launch {
                        val permission = ContextCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.CAMERA
                        )
                        if (permission == PackageManager.PERMISSION_GRANTED) {
                            cameraPermissionGranted = true
                            dialogViewModel.isPreviewing.value = true
                        } else {
                            ActivityCompat.requestPermissions(
                                mContext as Activity,
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
