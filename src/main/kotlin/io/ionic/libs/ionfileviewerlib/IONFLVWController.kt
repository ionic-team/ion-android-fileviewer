package io.ionic.libs.ionfileviewerlib

import android.app.Activity
import io.ionic.libs.ionfileviewerlib.helpers.IONFLVWAssetsHelper
import io.ionic.libs.ionfileviewerlib.helpers.IONFLVWInputsValidator
import io.ionic.libs.ionfileviewerlib.helpers.IONFLVWOpenDocumentHelper
import io.ionic.libs.ionfileviewerlib.helpers.runCatchingIONFLVWExceptions
import io.ionic.libs.ionfileviewerlib.model.IONFLVWException

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
     * TODO document method
     */
    fun openDocumentFromLocalPath(
        activity: Activity,
        filePath: String
    ): Result<Unit> = runCatchingIONFLVWExceptions {
        if (!inputsValidator.isPathValid(filePath)) {
            throw IONFLVWException.InvalidPath(filePath)
        }
        val filePathNoSpaces: String =
            filePath.replace("%20".toRegex(), "\\ ").replace(" ".toRegex(), "\\ ")
        openDocumentHelper.openDocumentFromLocalPath(activity, filePathNoSpaces)
    }

    /**
     * TODO document method
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
     * TODO document method
     */
    fun openDocumentFromResources(
        activity: Activity,
        assetsPath: String
    ): Result<Unit> = runCatchingIONFLVWExceptions {
        if (!inputsValidator.isPathValid(assetsPath)) {
            throw IONFLVWException.InvalidPath(assetsPath)
        } else if (inputsValidator.isInMainThread()) {
            throw IONFLVWException.ForbiddenOnMainThread()
        }
        val resourceLocalPath = assetsHelper.copyAssetToOpen(activity, assetsPath)
        openDocumentHelper.openDocumentFromLocalPath(activity, resourceLocalPath)
    }
}