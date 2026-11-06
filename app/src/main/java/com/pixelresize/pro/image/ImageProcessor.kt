package com.pixelresize.pro.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ImageInfo(
    val width: Int,
    val height: Int,
    val rotationDegrees: Int,
    val mimeType: String?
)

object ImageProcessor {

    const val MAX_DIMENSION = 12000

    const val MAX_PIXELS = 8_000_000L

    private const val MAX_SOURCE_DIMENSION = 100_000

    suspend fun inspectImage(
        context: Context,
        uri: Uri
    ): ImageInfo = withContext(Dispatchers.IO) {

        val bounds = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        context.contentResolver.openInputStream(uri).use { stream ->
            requireNotNull(stream) {
                "Não foi possível abrir a imagem."
            }

            BitmapFactory.decodeStream(stream, null, bounds)
        }

        require(
            bounds.outWidth > 0 &&
                bounds.outHeight > 0
        ) {
            "O ficheiro seleccionado não é uma imagem válida."
        }

        require(
            bounds.outWidth <= MAX_SOURCE_DIMENSION &&
                bounds.outHeight <= MAX_SOURCE_DIMENSION
        ) {
            "A imagem possui dimensões demasiado grandes."
        }

        val rotation = readRotation(context, uri)

        val rotated = rotation == 90 || rotation == 270

        ImageInfo(
            width = if (rotated) bounds.outHeight else bounds.outWidth,
            height = if (rotated) bounds.outWidth else bounds.outHeight,
            rotationDegrees = rotation,
            mimeType = context.contentResolver.getType(uri)
        )
    }

    suspend fun decodeImage(
        context: Context,
        uri: Uri,
        targetWidth: Int,
        targetHeight: Int
    ): Bitmap = withContext(Dispatchers.IO) {

        require(
            targetWidth in 1..MAX_DIMENSION &&
                targetHeight in 1..MAX_DIMENSION
        ) {
            "As dimensões devem estar entre 1 e $MAX_DIMENSION pixels."
        }

        require(
            targetWidth.toLong() * targetHeight <= MAX_PIXELS
        ) {
            "A resolução máxima permitida é de 8 megapixels."
        }

        val info = inspectImage(context, uri)

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        context.contentResolver.openInputStream(uri).use { stream ->
            requireNotNull(stream) {
                "Não foi possível ler a imagem."
            }

            BitmapFactory.decodeStream(stream, null, options)
        }

        val rotated =
            info.rotationDegrees == 90 ||
                info.rotationDegrees == 270

        val requiredWidth =
            if (rotated) targetHeight else targetWidth

        val requiredHeight =
            if (rotated) targetWidth else targetHeight

        var sampleSize = 1

        while (
            options.outWidth / (sampleSize * 2) >= requiredWidth &&
            options.outHeight / (sampleSize * 2) >= requiredHeight
        ) {
            sampleSize *= 2
        }

        while (
            (options.outWidth.toLong() / sampleSize) *
                (options.outHeight.toLong() / sampleSize) >
                MAX_PIXELS
        ) {
            sampleSize *= 2
        }

        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }

        val decoded = context.contentResolver
            .openInputStream(uri)
            .use { stream ->

                requireNotNull(stream) {
                    "Não foi possível descodificar a imagem."
                }

                BitmapFactory.decodeStream(
                    stream,
                    null,
                    decodeOptions
                )
            } ?: throw IllegalArgumentException(
                "Não foi possível descodificar a imagem."
            )

        var oriented: Bitmap? = null
        var output: Bitmap? = null

        try {
            val orientedBitmap = applyOrientation(
                decoded,
                info.rotationDegrees
            )

            oriented = orientedBitmap

            val resultBitmap = Bitmap.createScaledBitmap(
                orientedBitmap,
                targetWidth,
                targetHeight,
                true
            )

            output = resultBitmap

            resultBitmap

        } finally {

            // Preserva o Bitmap devolvido para a exportação.
            if (
                oriented != null &&
                oriented !== decoded &&
                oriented !== output &&
                !oriented.isRecycled
            ) {
                oriented.recycle()
            }

            if (
                decoded !== output &&
                !decoded.isRecycled
            ) {
                decoded.recycle()
            }
        }
    }

    private suspend fun readRotation(
        context: Context,
        uri: Uri
    ): Int = withContext(Dispatchers.IO) {

        context.contentResolver.openInputStream(uri).use { stream ->

            requireNotNull(stream) {
                "Não foi possível ler os metadados da imagem."
            }

            ExifInterface(stream).rotationDegrees
        }
    }

    private fun applyOrientation(
        bitmap: Bitmap,
        degrees: Int
    ): Bitmap {

        if (degrees == 0) return bitmap

        val matrix = Matrix().apply {
            postRotate(degrees.toFloat())
        }

        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }
}
