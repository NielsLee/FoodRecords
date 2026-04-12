package lying.fengfeng.foodrecords.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumberPickerWithButtons(
    modifier: Modifier = Modifier,
    initialNumber: Int = 0,
    minNumber: Int = 0,
    maxNumber: Int = Int.MAX_VALUE,
    expanded: Boolean = false,
    onNumberChange: (Int) -> Unit = {}
) {
    var number by remember { mutableIntStateOf(initialNumber) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        val iconSize = 24.dp
        val iconButtonSize = 24.dp

        if (expanded) {
            IconButton(
                onClick = {
                    number -= 20
                    if (number <= minNumber) {
                        number = 0
                        onNumberChange(number)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Remove,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
        }

        IconButton(
            onClick = {
                if (number > minNumber) {
                    number--
                }
                onNumberChange(number)
            },
            modifier = Modifier
                .clip(CircleShape)
                .size(iconButtonSize)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(imageVector = Icons.Filled.Remove, contentDescription = null)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = number.toString(),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            onClick = {
                if (number < maxNumber) {
                    number++
                    onNumberChange(number)
                }
            },
            modifier = Modifier
                .clip(CircleShape)
                .size(iconButtonSize)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }

        if (expanded) {
            IconButton(
                onClick = {
                    number += 20
                    if (number >= maxNumber) {
                        number = maxNumber
                    }
                    onNumberChange(number)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}