package lying.fengfeng.foodrecords.ui.settings.options

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lying.fengfeng.foodrecords.R


@Composable
fun AboutAppOption(
    iconSize: Dp,
    titleFontSize: TextUnit,
    titlePadding: PaddingValues
) {
    var infoExpanded by remember { mutableStateOf(false) }
    var wechatDialogShown by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(id = R.string.about_app),
                fontSize = titleFontSize,
                modifier = Modifier.padding(paddingValues = titlePadding),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .rotate(if (infoExpanded) 180f else 0f)
                    .clickable {
                        infoExpanded = !infoExpanded
                    },
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        AnimatedVisibility(visible = infoExpanded) {
            Column {
                Row(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Spacer(modifier = Modifier.size(iconSize))

                    Text(
                        text = stringResource(id = R.string.source_code_repo),
                        fontSize = titleFontSize,
                        modifier = Modifier.padding(paddingValues = titlePadding),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier.size(iconSize)
                    ) {
                        GitHubButton()
                    }
                }

                Row(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Spacer(modifier = Modifier.size(iconSize))
                    Text(
                        text = stringResource(id = R.string.contact_author),
                        fontSize = titleFontSize,
                        modifier = Modifier.padding(paddingValues = titlePadding),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            wechatDialogShown = true
                        },
                        modifier = Modifier.size(iconSize)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.wechat_svg),
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 6.dp))

                    Box(
                        modifier = Modifier.size(iconSize)
                    ) {
                        EmailButton()
                    }
                }

                Row(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Spacer(modifier = Modifier.size(iconSize))

                    Text(
                        text = stringResource(id = R.string.buy_author_coffee),
                        fontSize = titleFontSize,
                        modifier = Modifier.padding(paddingValues = titlePadding),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier.size(iconSize)
                    ) {
                        KofiButton()
                    }
                }
            }
        }
    }

    if (wechatDialogShown) {
        Dialog(
            onDismissRequest = { wechatDialogShown = false },
            content = {
                Card(
                    modifier = Modifier.wrapContentSize(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_wechat),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "(✿´‿`)")
                                Text(text = stringResource(R.string.author_notice))
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun EmailButton() {

    val context = LocalContext.current

    IconButton(onClick = {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("Niels_Lee@outlook.com"))
        }

        if (emailIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(emailIntent)
            Toast.makeText(
                context,
                context.getString(R.string.thanks_when_email),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, context.getString(R.string.no_email_app), Toast.LENGTH_SHORT)
                .show()
        }
    }
    ) {
        Icon(
            imageVector = Icons.Filled.Mail,
            contentDescription = null,
        )
    }
}

@Composable
fun GitHubButton() {

    val context = LocalContext.current

    IconButton(
        onClick = {
            val githubPackageName = "com.github.android"
            val githubRepoUrl = "https://github.com/NielsLee/FoodRecords"
            val githubAppUri = Uri.parse("github://github.com/NielsLee/FoodRecords")
            val webUri = Uri.parse(githubRepoUrl)

            val githubIntent = Intent(Intent.ACTION_VIEW, githubAppUri).apply {
                setPackage(githubPackageName)
            }

            val webIntent = Intent(Intent.ACTION_VIEW, webUri)

            if (isAppInstalled(context.packageManager, githubPackageName)) {
                try {
                    context.startActivity(githubIntent)
                } catch (e: Exception) {
                    context.startActivity(webIntent)
                }
            } else {
                context.startActivity(webIntent)
            }
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.github_mark_svg),
            contentDescription = null
        )
    }
}

@Composable
fun KofiButton() {
    val context = LocalContext.current

    val kofiUri = Uri.parse("https://ko-fi.com/nielslee")

    IconButton(onClick = {
        val webIntent = Intent(Intent.ACTION_VIEW, kofiUri)
        context.startActivity(webIntent)
    }) {
        Icon(Icons.Filled.Coffee, "Buy author a coffee")
    }
}

fun isAppInstalled(packageManager: PackageManager, packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}