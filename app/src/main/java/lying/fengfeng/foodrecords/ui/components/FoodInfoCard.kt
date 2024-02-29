package lying.fengfeng.foodrecords.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lying.fengfeng.foodrecords.ui.components.insertionDialog.createBitmap

@Composable
@Preview
fun FoodInfoCard() {
    Card(
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = "Food Name",
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )

        Row {


            Image(
                bitmap = createBitmap().asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
            )
            Column(
                Modifier.padding(horizontal = 8.dp)
            ) {

                Text(
                    text = "Food Info A"
                )

                Text(
                    text = "Food Info B"
                )

                Text(
                    text = "Food Info C"
                )

                Text(
                    text = "2",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        fontSize = 32.sp
                    )
                )
            }
        }
    }
}