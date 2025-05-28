package io.ionic.libs.ionfileviewerlib.helpers

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.io.FileNotFoundException

internal class IONFLVWOpenDocumentHelper {
    private val Activity.contentProviderAuthority: String
        get() = "$packageName.opener.provider"

    /**
     * Opens a file from a local file path
     *
     * @param activity the Android activity
     * @param filePath the full path to the file
     * @throws ActivityNotFoundException if no activity/app was found to open the file
     * @throws FileNotFoundException if the file does not exist
     */
    @Throws(ActivityNotFoundException::class, FileNotFoundException::class)
    fun openDocumentFromLocalPath(activity: Activity, filePath: String) {
        val mimeType = getMimeType(filePath)
        val file = File(filePath.replace("file:///", ""))
        if (!file.exists()) {
            throw FileNotFoundException()
        }

        val contentUri = FileProvider.getUriForFile(
            activity.applicationContext,
            activity.contentProviderAuthority,
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(contentUri, mimeType)
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        activity.startActivity(intent)
    }

    /**
     * Opens a file located at a remote URL.
     *
     * @param activity the Android activity
     * @param url the url string
     * @throws ActivityNotFoundException if no activity/app was found to open the file
     */
    @Throws(ActivityNotFoundException::class)
    fun openDocumentFromURL(activity: Activity, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        activity.startActivity(intent)
    }

    /**
     * gets a mime type based on the provided file
     *
     * @param filePath the full path to file
     * @return the mimeType or null if it was unable to determine
     */
    private fun getMimeType(filePath: String?): String? =
        MimeTypeMap.getFileExtensionFromUrl(filePath)?.let { extension ->
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
}
