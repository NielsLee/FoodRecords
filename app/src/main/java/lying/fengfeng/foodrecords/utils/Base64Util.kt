package lying.fengfeng.foodrecords.utils

import android.util.Base64
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object Base64Util {
    fun fileToBase64(filePath: String): String? {
        val file = File(filePath)
        if (!file.exists()) {
            println("File does not exist: $filePath")
            return null
        }

        try {
            val inputStream = FileInputStream(file)
            val byteArray = ByteArray(file.length().toInt())
            inputStream.read(byteArray)
            inputStream.close()

            // 灏嗗瓧鑺傛暟缁勮浆鎹负Base64缂栫爜
//            val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)

            // 濡傛灉闇€瑕佸幓闄ゆ崲琛岀锛屽彲浠ヤ娇鐢ㄤ笅闈㈣繖琛屼唬鏇夸笂闈㈢殑Base64.encodeToString()鏂规硶
            val base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP + Base64.NO_CLOSE)

            return base64
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun base64ToFile(base64Str: String, path: String) {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)

        val file = File(path)

        FileOutputStream(file).use { fos ->
            fos.write(decodedBytes)
            fos.flush()
        }
    }
}