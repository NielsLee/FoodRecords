package lying.fengfeng.foodrecords.backup

import android.content.Context
import android.content.Intent
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContract

/**
 * For selecting export file path
 */
class ExportActivityResultContract: ActivityResultContract<String, String>() {
    override fun createIntent(context: Context, input: String): Intent {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, "FoodRecords_${System.currentTimeMillis()}.csv")

            putExtra(DocumentsContract.EXTRA_INITIAL_URI, "/")
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        return intent?.dataString ?: "Empty"
    }
}