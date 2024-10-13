package lying.fengfeng.foodrecords.ui

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

expect object AppViewModelOwner: ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore
}