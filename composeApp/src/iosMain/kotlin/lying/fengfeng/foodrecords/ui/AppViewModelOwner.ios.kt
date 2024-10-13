package lying.fengfeng.foodrecords.ui

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

actual object AppViewModelOwner : ViewModelStoreOwner {
    private val appViewModelStore: ViewModelStore by lazy { ViewModelStore() }

    actual override val viewModelStore: ViewModelStore
        get() = appViewModelStore
}