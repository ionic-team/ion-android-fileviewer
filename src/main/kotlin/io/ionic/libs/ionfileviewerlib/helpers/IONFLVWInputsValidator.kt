package io.ionic.libs.ionfileviewerlib.helpers

import android.net.Uri
import android.os.Looper
import androidx.core.net.toUri
import io.ionic.libs.ionfileviewerlib.model.IONFLVWException
import java.util.regex.Pattern

class IONFLVWInputsValidator {
    /**
     * boolean method to check if a given file path is valid
     * @param path - the file path check
     * @return true if path is valid, false otherwise
     */
    fun isPathValid(path: String?): Boolean {
        return !path.isNullOrBlank()
    }

    /**
     * boolean method to check if a given url is valid
     * @param url - the url to check
     * @return true if url is valid, false otherwise
     */
    fun isURLValid(url: String): Boolean {
        val pattern =
            Pattern.compile("http[s]?://(([^/:.[:space:]]+(.[^/:.[:space:]]+)*)|([0-9](.[0-9]{3})))(:[0-9]+)?((/[^?#[:space:]]+)([^#[:space:]]+)?(#.+)?)?")
        return pattern.matcher(url).find()
    }

    /**
     * boolean method to check if the code is running on main thread
     * @return true if in main thread, false otherwise
     */
    fun isInMainThread(): Boolean = Looper.getMainLooper().isCurrentThread

    /**
     * Normalizes a file path before opening it.
     * This means verifying the fields are URL decoded, while making sure we're not trying to decode already decoded fields
     *
     * @param filePath the file path to normalize
     * @throws IONFLVWException.InvalidPath if unable to normalize the file path
     * @return the normalized file path, with special characters decoded.
     */
    fun normalizeFilePath(filePath: String): String {
        val filePathToNormalize = Uri.encode(Uri.decode(filePath))
        return filePathToNormalize.toUri().path ?: throw IONFLVWException.InvalidPath(filePath)
    }
}