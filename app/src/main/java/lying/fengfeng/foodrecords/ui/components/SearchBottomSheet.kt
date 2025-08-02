package lying.fengfeng.foodrecords.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBottomSheet(
    onDismiss: () -> Unit,
    onSearchFilterChange: (String?) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    var inputText by remember {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            OutlinedTextField(value = inputText,
                onValueChange = {
                    inputText = it
                },
                shape = IconButtonDefaults.outlinedShape,
                placeholder = { Text(text = context.getString(R.string.search_placeholder)) },
                modifier = Modifier.focusRequester(focusRequester)
            )

            Spacer(modifier = Modifier.weight(1f))
            
            Button(onClick = {
                onSearchFilterChange(inputText)
                onDismiss()
                if (inputText != "") {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = snackBarHostState.showSnackbar(
                            message = "${context.getString(R.string.search)}: $inputText",
                            actionLabel = context.getString(R.string.cancel),
                            duration = SnackbarDuration.Indefinite
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                onSearchFilterChange(null)
                            }

                            SnackbarResult.Dismissed -> {

                            }
                        }
                    }
                }
            }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
            }
        }

        LaunchedEffect(key1 = Unit) {
            focusRequester.requestFocus()
        }
    }
}