package com.pixelresize.pro

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

@Composable
fun ImagePreview(
    imageUri: Uri,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)

    SubcomposeAsyncImage(
        model = imageUri,
        contentDescription = "Pré-visualização da imagem",
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 340.dp)
            .aspectRatio(1.5f)
            .clip(shape)
            .background(SizeProPalette.Surface)
            .border(
                width = 1.dp,
                color = SizeProPalette.Border,
                shape = shape
            ),
        contentScale = ContentScale.Fit,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SizeProPalette.Surface),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        color = SizeProPalette.Violet,
                        trackColor = SizeProPalette.SurfaceVariant,
                        strokeWidth = 3.dp
                    )

                    Text(
                        text = "A carregar imagem...",
                        color = SizeProPalette.TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SizeProPalette.Surface),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.BrokenImage,
                        contentDescription = null,
                        tint = SizeProPalette.Pink,
                        modifier = Modifier
                            .then(Modifier)
                    )

                    Text(
                        text = "Não foi possível abrir esta imagem.",
                        color = SizeProPalette.TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        success = {
            SubcomposeAsyncImageContent(
                contentScale = ContentScale.Fit
            )
        }
    )
}
