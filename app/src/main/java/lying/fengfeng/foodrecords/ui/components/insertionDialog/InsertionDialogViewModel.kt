package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class InsertionDialogViewModel: ViewModel() {

    var isDialogShown = mutableStateOf(false)
    var isPreviewing = mutableStateOf(false)
}
