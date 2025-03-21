package io.ionic.libs.ionfileviewerlib

import android.app.Activity
import androidx.annotation.WorkerThread
import io.ionic.libs.ionfileviewerlib.helpers.IONFLVWAssetsHelper
import io.ionic.libs.ionfileviewerlib.helpers.IONFLVWInputsValidator
import io.ionic.libs.ionfileviewerlib.helpers.IONFLVWOpenDocumentHelper
import io.ionic.libs.ionfileviewerlib.helpers.runCatchingIONFLVWExceptions
import io.ionic.libs.ionfileviewerlib.model.IONFLVWException
import kotlin.concurrent.thread

/**
 * Entry point in IONFileViewerLib-Android
 *
 * Contains relevant methods for viewing files in Android.
 */
class IONFLVWController internal constructor(
    private val inputsValidator: IONFLVWInputsValidator,
    private val assetsHelper: IONFLVWAssetsHelper,
    private val openDocumentHelper: IONFLVWOpenDocumentHelper
) {
    constructor() : this(
        inputsValidator = IONFLVWInputsValidator(),
        assetsHelper = IONFLVWAssetsHelper(),
        openDocumentHelper = IONFLVWOpenDocumentHelper()
    )

    /**
     * Opens a document from a local file path.
     *
     * @param activity the Android activity
     * @param filePath the full path to the file
     * @return A Kotlin [Result] Object:
     *  - successful if the document was opened;
     *  - failure if an error occurred, exception of type [IONFLVWException]
     */
    fun openDocumentFromLocalPath(
        activity: Activity,
        filePath: String
    ): Result<Unit> = runCatchingIONFLVWExceptions {
        if (!inputsValidator.isPathValid(filePath)) {
            throw IONFLVWException.InvalidPath(filePath)
        }
        val filePathNoSpaces: String = filePath.replace("%20| ".toRegex(), "\\ ")
        openDocumentHelper.openDocumentFromLocalPath(activity, filePathNoSpaces)
    }

    /**
     * Opens a document from a remote url.
     *
     * @param activity the Android activity
     * @param url the url string
     * @return A Kotlin [Result] Object:
     *  - successful if the document was opened;
     *  - failure if an error occurred, exception of type [IONFLVWException]
     */
    fun openDocumentFromUrl(
        activity: Activity,
        url: String
    ): Result<Unit> = runCatchingIONFLVWExceptions {
        when {
            url.isBlank() -> throw IONFLVWException.EmptyURL(url)
            !inputsValidator.isURLValid(url) -> throw IONFLVWException.InvalidURL(url)
            else -> openDocumentHelper.openDocumentFromURL(activity, url)
        }
    }

    /**
     * Opens a document present in the app's resources (aka Assets).
     *
     * This method MUST NOT be called in the main thread.
     * The reason being that it has I/O operations that can cause the UI to freeze.
     * Ensure you run it from a background thread, e.g. using Kotlin's [kotlin.concurrent.thread] or coroutines.
     *
     * @param activity the Android activity
     * @param assetPath the relative path to the asset file in the `assets` folder of your app project
     * @return A Kotlin [Result] Object:
     *  - successful if the document was opened;
     *  - failure if an error occurred, exception of type [IONFLVWException]
     */
    @WorkerThread
    fun openDocumentFromResources(
        activity: Activity,
        assetPath: String
    ): Result<Unit> = runCatchingIONFLVWExceptions {
        if (!inputsValidator.isPathValid(assetPath)) {
            throw IONFLVWException.InvalidPath(assetPath)
        } else if (inputsValidator.isInMainThread()) {
            throw IONFLVWException.ForbiddenOnMainThread()
        }
        val resourceLocalPath =
            assetsHelper.copyAssetToOpen(activity.applicationContext, assetPath)
        openDocumentHelper.openDocumentFromLocalPath(activity, resourceLocalPath)
    }
}