package io.ionic.libs.ionfileviewerlib.helpers

import android.content.ActivityNotFoundException
import io.ionic.libs.ionfileviewerlib.model.IONFLVWException
import java.io.FileNotFoundException

internal inline fun <T> T.runCatchingIONFLVWExceptions(block: T.() -> Unit): Result<Unit> =
    runCatching(block).mapErrorToIONFLVWException()

internal fun <R> Result<R>.mapErrorToIONFLVWException(): Result<R> =
    exceptionOrNull()?.let { throwable ->
        val mappedException: IONFLVWException = when (throwable) {
            is IONFLVWException -> throwable
            is FileNotFoundException -> IONFLVWException.FileDoesNotExist(throwable)
            is ActivityNotFoundException -> IONFLVWException.NoApp(throwable)
            else -> IONFLVWException.UnknownError(throwable)
        }
        Result.failure(mappedException)
    } ?: this