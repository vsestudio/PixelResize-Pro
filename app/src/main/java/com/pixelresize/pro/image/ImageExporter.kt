
package com.pixelresize.pro.image

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object ImageExporter {

    suspend fun exportImage(
        context: Context,
        bitmap: android.graphics.Bitmap,
        destination: Uri,
        format: ImageFormat,
        quality: Int
    ): Long = withContext(Dispatchers.IO) {

        require(quality in 1..100) {
            "A qualidade deve estar entre 1 e 100."
        }

        val resolver = context.contentResolver

        val outputStream = resolver.openOutputStream(
            destination,
            "w"
        ) ?: throw IOException(
            "Não foi possível criar o ficheiro de destino."
        )

        outputStream.use { stream ->

            val successful = bitmap.compress(
                format.compressionFormat(),
                if (format == ImageFormat.PNG) 100 else quality,
                stream
            )

            if (!successful) {
                throw IOException(
                    "Não foi possível exportar a imagem."
                )
            }

            stream.flush()
        }

        val fileSize = resolver.query(
            destination,
            arrayOf(android.provider.OpenableColumns.SIZE),
            null,
            null,
            null
        )?.use { cursor ->

            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(
                    android.provider.OpenableColumns.SIZE
                )

                if (index >= 0 && !cursor.isNull(index)) {
                    cursor.getLong(index)
                } else {
                    -1L
                }
            } else {
                -1L
            }
        } ?: -1L

        fileSize
    }
}
