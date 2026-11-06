package com.pixelresize.pro.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.IOException
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageExporter {

    suspend fun exportImage(
        context: Context,
        bitmap: Bitmap,
        destination: Uri,
        format: ImageFormat,
        quality: Int
    ): Long = withContext(Dispatchers.IO) {

        require(!bitmap.isRecycled) {
            "A imagem já não está disponível na memória."
        }

        require(quality in 1..100) {
            "A qualidade deve estar entre 1 e 100."
        }

        val outputStream = context.contentResolver.openOutputStream(
            destination,
            "w"
        ) ?: throw IOException(
            "Não foi possível criar o ficheiro de destino."
        )

        val countingStream = CountingOutputStream(outputStream)

        countingStream.use { stream ->

            val successful = bitmap.compress(
                format.compressionFormat(),
                format.qualityForExport(quality),
                stream
            )

            if (!successful) {
                throw IOException(
                    "Não foi possível comprimir a imagem."
                )
            }

            stream.flush()
        }

        countingStream.bytesWritten
    }

    private class CountingOutputStream(
        private val output: OutputStream
    ) : OutputStream() {

        var bytesWritten: Long = 0L
            private set

        override fun write(value: Int) {
            output.write(value)
            bytesWritten++
        }

        override fun write(
            buffer: ByteArray,
            offset: Int,
            length: Int
        ) {
            output.write(buffer, offset, length)
            bytesWritten += length
        }

        override fun flush() {
            output.flush()
        }

        override fun close() {
            output.close()
        }
    }
}
