package com.pixelresize.pro.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

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

    /**
     * Lê as dimensões e a orientação EXIF
     * sem carregar a imagem completa na memória.
     */
    suspend fun inspectImage(
        context: Context,
        uri: Uri
    ): ImageInfo = withContext(Dispatchers.IO) {

        coroutineContext.ensureActive()

        val bounds = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        context.contentResolver.openInputStream(uri).use { stream ->

            requireNotNull(stream) {
                "Não foi possível abrir a imagem."
            }

            BitmapFactory.decodeStream(
                stream,
                null,
                bounds
            )
        }

        coroutineContext.ensureActive()

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
            width = if (rotated) {
                bounds.outHeight
            } else {
                bounds.outWidth
            },
            height = if (rotated) {
                bounds.outWidth
            } else {
                bounds.outHeight
            },
            rotationDegrees = rotation,
            mimeType = context.contentResolver.getType(uri)
        )
    }

    /**
     * Descodifica, corrige a orientação e redimensiona
     * a imagem para as dimensões solicitadas.
     */
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

        coroutineContext.ensureActive()

        val info = inspectImage(context, uri)

        val rotated =
            info.rotationDegrees == 90 ||
                info.rotationDegrees == 270

        // Dimensões antes da aplicação da rotação EXIF.
        val requiredWidth = if (rotated) {
            targetHeight
        } else {
            targetWidth
        }

        val requiredHeight = if (rotated) {
            targetWidth
        } else {
            targetHeight
        }

        // Obter dimensões originais sem descodificar a imagem.
        val bounds = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        context.contentResolver.openInputStream(uri).use { stream ->

            requireNotNull(stream) {
                "Não foi possível ler a imagem."
            }

            BitmapFactory.decodeStream(
                stream,
                null,
                bounds
            )
        }

        coroutineContext.ensureActive()

        require(
            bounds.outWidth > 0 &&
                bounds.outHeight > 0
        ) {
            "Não foi possível identificar as dimensões da imagem."
        }

        /*
         * Calcular uma amostragem adequada.
         *
         * O objectivo é evitar descodificar uma imagem
         * muito maior do que a resolução necessária.
         */
        var sampleSize = 1

        while (
            bounds.outWidth / (sampleSize * 2) >= requiredWidth &&
            bounds.outHeight / (sampleSize * 2) >= requiredHeight
        ) {
            sampleSize *= 2
        }

        /*
         * Limitar a memória utilizada durante a descodificação.
         */
        while (
            (bounds.outWidth.toLong() / sampleSize) *
                (bounds.outHeight.toLong() / sampleSize) >
                MAX_PIXELS
        ) {
            sampleSize *= 2
        }

        coroutineContext.ensureActive()

        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
            inScaled = false
            inDither = false
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

            coroutineContext.ensureActive()

            // Corrigir a orientação da fotografia.
            val orientedBitmap = applyOrientation(
                decoded,
                info.rotationDegrees
            )

            oriented = orientedBitmap

            coroutineContext.ensureActive()

            // Aplicar as dimensões escolhidas pelo utilizador.
            val resultBitmap = Bitmap.createScaledBitmap(
                orientedBitmap,
                targetWidth,
                targetHeight,
                true
            )

            output = resultBitmap

            coroutineContext.ensureActive()

            resultBitmap

        } finally {

            /*
             * Libertar as imagens intermédias.
             * O Bitmap devolvido permanece disponível
             * para o ImageExporter.
             */

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

    /**
     * Ler a orientação EXIF da fotografia.
     */
    private suspend fun readRotation(
        context: Context,
        uri: Uri
    ): Int = withContext(Dispatchers.IO) {

        context.contentResolver.openInputStream(uri).use { stream ->

            requireNotNull(stream) {
                "Não foi possível ler os metadados da imagem."
            }

            val degrees = ExifInterface(stream).rotationDegrees

            if (degrees in listOf(0, 90, 180, 270)) {
                degrees
            } else {
                0
            }
        }
    }

    /**
     * Aplicar a rotação EXIF sem modificar o Bitmap original.
     */
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
