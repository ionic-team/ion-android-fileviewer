package io.ionic.libs.ionfileviewerlib.helpers

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException

internal class IONFLVWOpenDocumentHelper {
    private val Activity.contentProviderAuthority: String
        get() = "$packageName.opener.provider"

    @Throws(ActivityNotFoundException::class, FileNotFoundException::class)
    fun openDocumentFromLocalPath(activity: Activity, filePath: String) {
        val mimeType = getMimeType(filePath)
        val file = File(filePath.replace("file:///", ""))

        if (file.exists()) {
            val contentUri = FileProvider.getUriForFile(
                activity.applicationContext,
                activity.contentProviderAuthority,
                file
            )

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(contentUri, mimeType)
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            activity.startActivity(intent)
        } else {
            throw FileNotFoundException()
        }
    }

    @Throws(ActivityNotFoundException::class)
    fun openDocumentFromURL(activity: Activity, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(intent)
    }

    private fun getMimeType(filePath: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}
