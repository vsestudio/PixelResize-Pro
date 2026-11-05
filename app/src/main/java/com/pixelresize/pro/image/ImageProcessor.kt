
package com.pixelresize.pro.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

data class ImageInfo(
    val width: Int,
    val height: Int,
    val rotationDegrees: Int,
    val mimeType: String?
)

object ImageProcessor {

    const val MAX_DIMENSION = 12000
    const val MAX_PIXELS = 40_000_000L

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

        require(bounds.outWidth > 0 && bounds.outHeight > 0) {
            "O ficheiro seleccionado não é uma imagem válida."
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

        require(targetWidth in 1..MAX_DIMENSION) {
            "A largura deve estar entre 1 e $MAX_DIMENSION pixels."
        }

        require(targetHeight in 1..MAX_DIMENSION) {
            "A altura deve estar entre 1 e $MAX_DIMENSION pixels."
        }

        require(targetWidth.toLong() * targetHeight <= MAX_PIXELS) {
            "A resolução máxima permitida é 40 megapixels."
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

        val rotated = info.rotationDegrees == 90 ||
            info.rotationDegrees == 270

        val requiredWidth = if (rotated) targetHeight else targetWidth
        val requiredHeight = if (rotated) targetWidth else targetHeight

        var sampleSize = 1

        while (
            options.outWidth / (sampleSize * 2) >= requiredWidth &&
            options.outHeight / (sampleSize * 2) >= requiredHeight
        ) {
            sampleSize *= 2
        }

        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }

        val decoded = context.contentResolver.openInputStream(uri).use { stream ->
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

        val oriented = applyOrientation(
            decoded,
            info.rotationDegrees
        )

        val resized = Bitmap.createScaledBitmap(
            oriented,
            targetWidth,
            targetHeight,
            true
        )

        if (oriented !== decoded && !decoded.isRecycled) {
            decoded.recycle()
        }

        if (resized !== oriented && !oriented.isRecycled) {
            oriented.recycle()
        }

        resized
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
