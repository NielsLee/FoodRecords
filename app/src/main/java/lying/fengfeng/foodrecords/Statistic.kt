import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.BuildConfig
import java.util.Locale

object Statistic {
    private val backendHost = BuildConfig.BACKEND_HOST
    private val client = OkHttpClient()
    private val language = Locale.getDefault().language
    private val version = BuildConfig.VERSION_NAME

    fun uploadOpenData(uuid: String, open_time: String) {
        val url = "http://${backendHost}/statistic/"
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val jsonBody = """
            {
                "uuid": "$uuid",
                "language": "$language",
                "open_time": "$open_time",
                "version": "$version"
            }
        """.trimIndent()

        val body = jsonBody.toRequestBody(mediaType)
        Log.i("uploadOpenData", jsonBody)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("uploadOpenData",  "error: $response")
                }
                Log.e("uploadOpenData", "response is $response")
            }
        }
    }
}