
package com.pixelresize.pro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pixelresize.pro.data.ImageResult

@Composable
fun ResultScreen(
    result: ImageResult,
    onShare: () -> Unit,
    onResizeAgain: () -> Unit,
    onUploadAnother: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Imagem processada!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        ImagePreview(
            imageUri = result.uri
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = "Detalhes do resultado",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                ResultDetail(
                    label = "Resolução",
                    value = result.resolution
                )

                ResultDetail(
                    label = "Tamanho do ficheiro",
                    value = result.formattedFileSize
                )

                ResultDetail(
                    label = "Formato",
                    value = result.format.displayName
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onShare,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Partilhar imagem")
        }

        OutlinedButton(
            onClick = onResizeAgain,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Redimensionar novamente")
        }

        OutlinedButton(
            onClick = onUploadAnother,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Escolher outra imagem")
        }
    }
}

@Composable
private fun ResultDetail(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold
        )
    }
}
