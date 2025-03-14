# ion-android-fileviewer

File viewer native Android library ⚡️

Documentation to be added.


```xml
<manifest>
    <!-- permissions and other -->
    <application>
        <!-- activities and other declarations... -->
        <provider
            android:name="io.ionic.libs.ionfileviewerlib.IONFLVWFileProvider"
            android:authorities="${applicationId}.opener.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/ionfileviewer_paths" />
        </provider>
    </application>
</manifest>
```

# ion-android-fileviewer

The `ion-android-fileviewer` is a library built using `Kotlin` that wraps Android `Intent` to and view files.

Use the `IONFLVWController` class to open files, from local paths, app's assets, and remote URL's.


## Index

- [Motivation](#motivation)
- [Usage](#usage)
- [Methods](#methods)
    - [Open document from local file path](#open-document-from-local-file-path)
    - [Open document from a remote URL](#open-document-from-a-remote-url)
    - [Open document from app's assets](#open-document-from-apps-assets)
- [Errors](#errors)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)
- [Support](#support)

## Motivation

This library is used by File Viewer Plugins - for both [OutSystems' Cordova Plugin](https://github.com/ionic-team/cordova-outsystems-fileviewer) and [Ionic's Capacitor Plugin](https://github.com/ionic-team/capacitor-file-viewer).

## Usage

In your app-level gradle file, import the `ion-android-fileviewer` library like so:

```
dependencies {
    implementation("io.ionic.libs:ionfileviewer-android:1.0.0")
}
```

## Methods

This section details the methods provided by the `IONFLVWController`:

### Open document from local file path

```kotlin
fun openDocumentFromLocalPath(
    activity: Activity,
    filePath: String
): Result<Unit>
```

The method is composed of the following input parameters:
- **activity**: the `Activity` from the app using the library to use when for opening the file in another app.
- **filePath**: `String` with the full path to the file.

The method returns a `Result`, that is either success (no data is returned) if file was opened, or an [exception](#errors) that should be handled by the caller app.

### Open document from a remote URL

```kotlin
fun openDocumentFromUrl(
    activity: Activity,
    url: String
): Result<Unit>
```

The method is composed of the following input parameters:
- **activity**: the `Activity` from the app using the library to use when for opening the url in another app.
- **url**: `String` with the url to open.

The method returns a `Result`, that is either success (no data is returned) if url was opened, or an [exception](#errors) that should be handled by the caller app.


### Open document from app's assets

```kotlin
@WorkerThread
fun openDocumentFromResources(
    activity: Activity,
    assetPath: String
): Result<Unit>
```

The method is composed of the following input parameters:
- **activity**: the `Activity` from the app using the library to use when for opening the file in another app.
- **assetPath**: `String` with the relative path to the file inside the `assets` folder.

The method returns a `Result`, that is either success (no data is returned) if file was opened, or an [exception](#errors) that should be handled by the caller app.

Unlike the other methods, this cannot be called from the main thread, because it contains I/O operations that could freeze the UI.
You may create a thread using Kotlin's [`thread`](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.concurrent/thread.html) function, or use [coroutines](https://kotlinlang.org/docs/coroutines-overview.html), as exemplified below:

```kotlin
val coroutineScope = CoroutineScope(Dispatchers.IO)
val fileViewerController = IONFLVWController()
coroutineScope.launch {
    fileViewerController.openDocumentFromResources(activity, assetPath)
        .onSuccess {
            // handle file open successfully here
        }
        .onError {
            // handle errors here
        }
}
```

## Errors

The exceptions returned by the plugin method are all of type `IONFLVWException`.

Some exceptions may have a `cause` in case it was thrown outside the controller (e.g. by Android OS).

## Contributing

1. Fork the repository (Unselect "Copy only `main` branch")
2. Checkout development branch.
3. Create your feature branch (`git checkout -b feature/amazing-feature`)
4. Commit your changes (`git commit -m 'Add amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request to development.

## License

`ion-android-fileviewer` is available under the MIT license. See the [LICENSE](LICENSE) file for more info.

## Support

- Report issues on our [Issue Tracker](https://github.com/ionic-team/ion-android-fileviewer/issues)