package io.ionic.libs.ionfileviewerlib.helpers

import android.os.Looper
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
        val matcher = pattern.matcher(url)
        return matcher.find()
    }

    /**
     * boolean method to check if the code is running on main thread
     * @return true if in main thread, false otherwise
     */
    fun isInMainThread(): Boolean = Looper.getMainLooper().isCurrentThread
}