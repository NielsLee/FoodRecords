package lying.fengfeng.foodrecords.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class HomeViewModel: ViewModel() {

    var intList = mutableStateListOf<Int>()

    fun updateList(value: Int) {
        intList.add(value)
    }
}