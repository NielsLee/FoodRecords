package lying.fengfeng.foodrecords.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {


    val list  = remember { viewModel.intList }

//    LazyVerticalGrid(
//        GridCells.Fixed(2),
//    ) {
//
//        items(list.size) { index ->
//
//            Text("Item $index")
//        }
//    }

    Text(text = "This is home ${list.toList()}")

}