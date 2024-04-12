package lying.fengfeng.foodrecords.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.ui.components.insertionDialog.createBitmap

@Composable
@Preview
fun FoodInfoCard(
    @PreviewParameter(provider = FoodNamePreviewProvider::class)
    foodInfo: FoodInfo,
    modifier: Modifier = Modifier.width(300.dp).height(400.dp)
) {
    Card(
        modifier = modifier
    ) {
        Text(
            text = foodInfo.foodName,
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontSize = 36.sp 
            ),
            textAlign = TextAlign.Center
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
                    text = foodInfo.foodType,
                )

                Text(
                    text = foodInfo.productionDate,
                )

                Text(
                    text = foodInfo.shelfLife,
                )
            }
        }

        Row(

        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "2",
                    modifier = Modifier,
                    style = TextStyle(
                        fontSize = 64.sp
                    )
                )
            }

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(Icons.Filled.ArrowDropDownCircle, null, modifier = Modifier.size(64.dp))
                }
            }
        }
    }
}

class FoodNamePreviewProvider : PreviewParameterProvider<FoodInfo> {
    // 提供预览参数值
    override val values: Sequence<FoodInfo> = sequenceOf(
        FoodInfo("FoodName", "2022-02-02", "CXK", "7days", "uuid",)
    )
}