package com.pixelresize.pro.data

import android.net.Uri
import com.pixelresize.pro.image.ImageFormat
import java.util.Locale

data class ImageResult(
    val uri: Uri,
    val width: Int,
    val height: Int,
    val fileSizeBytes: Long,
    val format: ImageFormat
) {

    val resolution: String
        get() = "$width × $height px"

    val formattedFileSize: String
        get() {
            val size = fileSizeBytes.coerceAtLeast(0L)

            return when {
                size < 1024L ->
                    "$size B"

                size < 1024L * 1024L ->
                    String.format(
                        Locale.getDefault(),
                        "%.1f KB",
                        size / 1024.0
                    )

                size < 1024L * 1024L * 1024L ->
                    String.format(
                        Locale.getDefault(),
                        "%.2f MB",
                        size / (1024.0 * 1024.0)
                    )

                else ->
                    String.format(
                        Locale.getDefault(),
                        "%.2f GB",
                        size / (1024.0 * 1024.0 * 1024.0)
                    )
            }
        }

    val hasValidDimensions: Boolean
        get() = width > 0 && height > 0

    val totalPixels: Long
        get() = width.toLong() * height.toLong()
}
