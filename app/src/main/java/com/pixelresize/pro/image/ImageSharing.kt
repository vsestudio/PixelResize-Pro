
package com.pixelresize.pro.image

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri

object ImageSharing {

    fun shareImage(
        context: Context,
        imageUri: Uri,
        format: ImageFormat
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {

            type = format.mimeType

            putExtra(
                Intent.EXTRA_STREAM,
                imageUri
            )

            clipData = ClipData.newUri(
                context.contentResolver,
                "Size Pro image",
                imageUri
            )

            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        val chooser = Intent.createChooser(
            shareIntent,
            "Partilhar imagem — Size Pro"
        ).apply {

            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        context.startActivity(chooser)
    }
}
