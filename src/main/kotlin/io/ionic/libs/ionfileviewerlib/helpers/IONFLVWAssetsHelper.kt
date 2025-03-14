package io.ionic.libs.ionfileviewerlib.helpers

import android.content.Context
import android.content.res.AssetManager
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

internal class IONFLVWAssetsHelper {
    /**
     * Copy an asset to open with the File Viewer library
     *
     * The assets aren't opened directly where they are located.
     * They must be copied to a directory that is included in the File Provider paths.
     *
     * @param context the Android context - to access the assets
     * @param fileToBeCopied path to the android asset;
     * if asset is located in root of `assets` folder in Android, then the file name suffices
     * @return the file path to the copied asset, to use with File Provider.
     */
    fun copyAssetToOpen(context: Context, fileToBeCopied: String): String {
        val assetManager: AssetManager = context.assets
        val assetsLocation = fileToBeCopied.substringBeforeLast("/", missingDelimiterValue = "")
        val assetFiles: Array<String> = assetManager.list(assetsLocation) ?: emptyArray()
        val fileName = fileToBeCopied.substringAfterLast("/")
        // confirm that the resource file exists, otherwise cannot proceed
        assetFiles.firstOrNull { it == fileName } ?: throw FileNotFoundException()
        val outFile = File(context.externalCacheDir, fileName)
        assetManager.open(fileToBeCopied).use { inputStream ->
            FileOutputStream(outFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return outFile.absolutePath
    }
}