package io.ionic.libs.ionfileviewerlib.helpers

import android.app.Activity
import android.content.res.AssetManager
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

internal class IONFLVWAssetsHelper {
    /**
     *
     */
    fun copyAssetToOpen(activity: Activity, fileToBeCopied: String): String {
        val context = activity.applicationContext
        val assetManager: AssetManager = activity.assets
        val assetsLocation = fileToBeCopied.substringBeforeLast("/")
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