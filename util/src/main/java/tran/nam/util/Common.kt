@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package tran.nam.util

import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

private fun removeFileNameForCheckMimeTypePurpose(s: String): String {
    if (s.isEmpty() || !s.contains('.')) {
        return s
    }
    return s.replaceRange(0, s.lastIndexOf('.'), "filename")
}

fun getMimeType(url: String): String {
    var type = "*/*"
    val extension = MimeTypeMap.getFileExtensionFromUrl(removeFileNameForCheckMimeTypePurpose(url))
    if (extension != null) {
        try {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        } catch (exception: IllegalStateException) {
            Logger.debug("MimeTypeMap.getSingleton…eFromExtension(extension) must not be null")
        }
    }
    Logger.debug(type)
    return type
}

fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
    val requestFile = RequestBody.create(
        MediaType.parse(getMimeType(file.absolutePath)),
        file
    )
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)
}

fun createPartFromString(name: String, value: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(name, value)
}