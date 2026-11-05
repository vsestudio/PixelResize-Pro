
package com.pixelresize.pro.data

import android.net.Uri
import com.pixelresize.pro.image.ImageFormat

data class ImageResult(
    val uri: Uri,
    val width: Int,
    val height: Int,
    val fileSizeBytes: Long,
    val format: ImageFormat
) {
    val resolution: String
        get() = "${width} × ${height} px"

    val formattedFileSize: String
        get() = when {
            fileSizeBytes < 1024 ->
                "$fileSizeBytes B"

            fileSizeBytes < 1024 * 1024 ->
                "%.1f KB".format(
                    fileSizeBytes / 1024.0
                )

            else ->
                "%.2f MB".format(
                    fileSizeBytes / (1024.0 * 1024.0)
                )
        }
}
