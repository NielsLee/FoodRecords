package lying.fengfeng.foodrecords.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "This is Settings", modifier = Modifier)

        Card(
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                
                Icon(imageVector = Icons.Filled.Face, contentDescription = null)

                Text(text = "This is 作者")
            }
        }
    }
}