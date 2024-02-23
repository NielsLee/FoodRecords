package lying.fengfeng.foodrecords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lying.fengfeng.foodrecords.ui.compose.InsertionDialogCompose
import lying.fengfeng.foodrecords.ui.compose.Screen
import lying.fengfeng.foodrecords.ui.theme.FoodRecordsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {

    val intList = remember {
        mutableStateOf(listOf<Int>())
    }

    val selectedScreen = remember {
        mutableStateOf(Screen.HomeFragment)
    }

    val showDialog = remember {
        mutableStateOf(false)
    }


    FoodRecordsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        // TODO 想办法让状态栏和appBar的颜色一致，而不是反过来
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        when(selectedScreen.value) {
                            Screen.HomeFragment -> {
                                Text("Home")
                            }
                            Screen.ListFragment -> {
                                Text("List")
                            }
                            Screen.SettingsFragment -> {
                                Text("Settings")
                            }
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { selectedScreen.value = Screen.HomeFragment }) {
                            Icon(
                                Icons.Filled.Home,
                                contentDescription = "Localized description")
                        }
                        IconButton(onClick = { selectedScreen.value = Screen.ListFragment }) {
                            Icon(
                                Icons.Filled.List,
                                contentDescription = "Localized description",
                            )
                        }
                        IconButton(onClick = { selectedScreen.value = Screen.SettingsFragment }) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "Localized description",
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
//                                        intList.value = intList.value.toMutableList().also {
//                                            it.add(888)
//                                        }
                                showDialog.value = true
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.Add, "Localized description")
                        }
                    }
                )
            }

        ) { paddingValues ->

            when(selectedScreen.value) {
                Screen.HomeFragment -> {
                    LazyVerticalGrid(
                        GridCells.Fixed(2),
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        items(intList.value) { index ->
                            Text("Item $index", modifier = Modifier.padding(8.dp))
                        }
                    }
                    if(showDialog.value) {
                        InsertionDialogCompose.InsertionDialog {
                            showDialog.value = false
                        }
                    }
                }
                Screen.ListFragment -> {
                    Greeting(name = "List", modifier = Modifier.padding(paddingValues))

                }
                Screen.SettingsFragment -> {
                    Greeting(name = "Settings", modifier = Modifier.padding(paddingValues))

                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApp()
}