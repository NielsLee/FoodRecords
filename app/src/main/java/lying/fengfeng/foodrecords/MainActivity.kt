package lying.fengfeng.foodrecords

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.backup.ExportActivityResultContract
import lying.fengfeng.foodrecords.backup.ImportActivityResultContract
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.ui.FoodRecordsApp
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import lying.fengfeng.foodrecords.utils.Base64Util
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : ComponentActivity() {

    lateinit var exportLauncher: ActivityResultLauncher<String>
    lateinit var importLauncher: ActivityResultLauncher<String>
    private lateinit var appViewModel: FoodRecordsAppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodRecordsApp()
        }

        appViewModel = ViewModelProvider(this)[FoodRecordsAppViewModel::class.java]

        exportLauncher = registerForActivityResult(ExportActivityResultContract()) { contentUriStr ->
            lifecycleScope.launch {
                val descriptor = contentResolver.openFileDescriptor(contentUriStr.toUri(), "w")
                val foodInfoList = withContext(Dispatchers.IO) {
                    AppRepo.getAllFoodInfo().map { foodInfo ->
                        listOf(
                            foodInfo.uuid,
                            foodInfo.foodName,
                            foodInfo.foodType,
                            foodInfo.productionDate,
                            foodInfo.shelfLife,
                            foodInfo.expirationDate,
                            foodInfo.tips,
                            Base64Util.fileToBase64(AppRepo.getPicturePath(foodInfo.uuid))
                            )
                    }
                }
                try {
                    val outputStream = FileOutputStream(descriptor?.fileDescriptor)
                    val writer = csvWriter {
                        charset = "GBK"
                        lineTerminator = "\n"
                    }
                    writer.open(outputStream) {
                        writeRows(foodInfoList)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    descriptor?.close()
                }
            }
        }

        importLauncher = registerForActivityResult(ImportActivityResultContract()) { contentUriStr ->
            lifecycleScope.launch {
                val descriptor = contentResolver.openFileDescriptor(contentUriStr.toUri(), "r")
                try {
                    val inputStream = FileInputStream(descriptor?.fileDescriptor)
                    var lineList: List<List<String>> = listOf()
                    val foodInfoList = mutableListOf<FoodInfo>()
                    val reader = csvReader {
                        charset = "GBK"
                    }
                    reader.open(inputStream) {
                        lineList = readAllAsSequence().toList()
                    }

                    withContext(Dispatchers.IO) {
                        for (line in lineList) {
                            FoodInfo(
                                uuid = line[0],
                                foodName = line[1],
                                foodType = line[2],
                                productionDate = line[3],
                                shelfLife = line[4],
                                expirationDate = line[5],
                                tips = line[6]
                            ).also {
                                foodInfoList.add(it)
                                appViewModel.addFoodInfo(it)
                            }
                            Base64Util.base64ToFile(line[7], AppRepo.getPicturePath(line[0]))
                        }
                    }

                    Toast.makeText(this@MainActivity, "+${lineList.size}", Toast.LENGTH_SHORT).show()

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    descriptor?.close()
                }
            }
        }
    }
}