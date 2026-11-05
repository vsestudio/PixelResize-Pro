
package com.pixelresize.pro

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

@Composable
fun ImagePreview(
    imageUri: Uri,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = imageUri,
        contentDescription = "Pré-visualização da imagem",
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 340.dp)
            .aspectRatio(1.5f)
            .clip(RoundedCornerShape(18.dp)),
        contentScale = ContentScale.Fit,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "Não foi possível abrir esta imagem.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        success = {
            SubcomposeAsyncImageContent()
        }
    )
}
