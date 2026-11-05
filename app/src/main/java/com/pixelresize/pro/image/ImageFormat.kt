
package com.pixelresize.pro.image

import android.graphics.Bitmap

enum class ImageFormat(
    val displayName: String,
    val extension: String,
    val mimeType: String
) {
    JPEG(
        displayName = "JPG",
        extension = "jpg",
        mimeType = "image/jpeg"
    ),

    PNG(
        displayName = "PNG",
        extension = "png",
        mimeType = "image/png"
    ),

    WEBP(
        displayName = "WebP",
        extension = "webp",
        mimeType = "image/webp"
    );

    fun compressionFormat(): Bitmap.CompressFormat {
        return when (this) {
            JPEG -> Bitmap.CompressFormat.JPEG
            PNG -> Bitmap.CompressFormat.PNG

            WEBP -> {
                if (android.os.Build.VERSION.SDK_INT >= 30) {
                    Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    @Suppress("DEPRECATION")
                    Bitmap.CompressFormat.WEBP
                }
            }
        }
    }
}
