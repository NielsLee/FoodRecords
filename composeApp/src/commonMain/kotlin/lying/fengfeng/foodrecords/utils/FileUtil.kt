package lying.fengfeng.foodrecords.utils

expect object FileUtil {
    fun isFileExist(filePath: String): Boolean

    fun getPictureFilePath(uuid: String): String
}