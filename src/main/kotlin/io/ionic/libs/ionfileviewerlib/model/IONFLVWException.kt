package io.ionic.libs.ionfileviewerlib.model

/**
 * The available exceptions that the File Viewer library can return.
 * Some of the exceptions can return a cause in case it was triggered by another source (e.g. Android OS)
 */
sealed class IONFLVWException(
    override val message: String,
    override val cause: Throwable? = null
) : Throwable(message, cause) {

    class ForbiddenOnMainThread :
        IONFLVWException("The operation cannot be run in the main thread")

    class InvalidPath(val path: String?) :
        IONFLVWException("The provided path is either null or empty.")

    class EmptyURL(val url: String?) :
        IONFLVWException("The provided url is either null or empty.")

    class InvalidURL(val url: String) : IONFLVWException("The provided url is not valid.")

    class FileDoesNotExist(override val cause: Throwable? = null) :
        IONFLVWException("The specified file does not exist", cause)

    class NoApp(override val cause: Throwable?) :
        IONFLVWException("There is no app to open the specified file", cause)

    class UnknownError(override val cause: Throwable?) :
        IONFLVWException("An unknown error occurred while trying to run the operation", cause)
}