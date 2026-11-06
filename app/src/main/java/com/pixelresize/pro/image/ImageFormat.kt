package com.pixelresize.pro.image

import android.graphics.Bitmap
import android.os.Build

enum class ImageFormat(
    val displayName: String,
    val extension: String,
    val mimeType: String,
    val supportsTransparency: Boolean,
    val supportsQuality: Boolean
) {

    JPEG(
        displayName = "JPG",
        extension = "jpg",
        mimeType = "image/jpeg",
        supportsTransparency = false,
        supportsQuality = true
    ),

    PNG(
        displayName = "PNG",
        extension = "png",
        mimeType = "image/png",
        supportsTransparency = true,
        supportsQuality = false
    ),

    WEBP(
        displayName = "WebP",
        extension = "webp",
        mimeType = "image/webp",
        supportsTransparency = true,
        supportsQuality = true
    );

    fun compressionFormat(): Bitmap.CompressFormat {
        return when (this) {
            JPEG -> Bitmap.CompressFormat.JPEG

            PNG -> Bitmap.CompressFormat.PNG

            WEBP -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    @Suppress("DEPRECATION")
                    Bitmap.CompressFormat.WEBP
                }
            }
        }
    }

    fun qualityForExport(quality: Int): Int {
        return if (supportsQuality) {
            quality.coerceIn(1, 100)
        } else {
            100
        }
    }
}
