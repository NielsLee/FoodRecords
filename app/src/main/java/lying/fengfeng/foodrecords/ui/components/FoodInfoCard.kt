package lying.fengfeng.foodrecords.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.repository.FoodInfoRepo
import lying.fengfeng.foodrecords.ui.components.insertionDialog.createBitmap
import lying.fengfeng.foodrecords.utils.DateUtil
import lying.fengfeng.foodrecords.utils.ImageUtil

@Composable
fun FoodInfoCard(
    foodInfo: FoodInfo,
    modifier: Modifier = Modifier,
    onDestroy: (() -> Unit)
) {

    val mContext = LocalContext.current

    Card(
        modifier = modifier
            .padding(3.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
            )
    ) {
        Text(
            text = foodInfo.foodName,
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontSize = 24.sp
            ),
            textAlign = TextAlign.Center
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            val foodPicturePath = FoodInfoRepo.getPicturePath(foodInfo.pictureUUID)
            val bitmap = ImageUtil.preProcessImage(foodPicturePath)

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(start = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
            ) {
                Image(
                    bitmap = bitmap?.asImageBitmap() ?: createBitmap().asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            RemainingDaysWindow(productionDate = foodInfo.productionDate, shelfLife = foodInfo.shelfLife)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {

            Column(
                Modifier.padding(horizontal = 8.dp)
            ) {

                Text(
                    text = foodInfo.foodType,
                )

                Text(
                    text = foodInfo.productionDate,
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

            IconButton(onClick = {
                onDestroy.invoke()
            }) {
                Icon(Icons.Filled.Delete, null, modifier = Modifier.size(64.dp))
            }
        }
    }
}

@Composable
fun RemainingDaysWindow(
    productionDate: String,
    shelfLife: String,
) {
    val remainingDays = DateUtil.getRemainingDays(productionDate, shelfLife)
    val mContext = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (remainingDays > 0) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .border(2.dp, Color.Green, shape = RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = mContext.getString(R.string.best_before),
                        modifier = Modifier.padding(4.dp),
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = remainingDays.toString(),
                    modifier = Modifier,
                    style = TextStyle(
                        fontSize = 48.sp,
                        color = Color.Green
                    )
                )
                Text(text = mContext.getString(R.string.shelf_life_day))
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .border(2.dp, Color.Red, shape = RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = mContext.getString(R.string.expired),
                        modifier = Modifier.padding(4.dp),
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = (-remainingDays).toString(),
                    modifier = Modifier,
                    style = TextStyle(
                        fontSize = 48.sp,
                        color = Color.Red
                    )
                )
                
                Text(text = mContext.getString(R.string.shelf_life_day))
            }
        }
    }
}