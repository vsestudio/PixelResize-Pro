
package com.pixelresize.pro

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val HeaderGradient = Brush.linearGradient(
    colors = listOf(
        SizeProPalette.Orange,
        SizeProPalette.Pink,
        SizeProPalette.Violet
    )
)

private val ActionGradient = Brush.horizontalGradient(
    colors = listOf(
        SizeProPalette.Orange,
        Color(0xFFFF6B70),
        SizeProPalette.Pink
    )
)

private val BorderGradient = Brush.horizontalGradient(
    colors = listOf(
        SizeProPalette.Orange,
        SizeProPalette.Pink,
        SizeProPalette.Violet,
        SizeProPalette.Blue
    )
)

@Composable
fun HomeScreen(
    onImageSelected: (Uri) -> Unit = {}
) {
    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let(onImageSelected)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // CABEÇALHO

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .background(HeaderGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SP",
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Size ",
                        fontSize = 29.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SizeProPalette.TextPrimary
                    )

                    Text(
                        text = "Pro",
                        fontSize = 29.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SizeProPalette.Pink
                    )
                }

                Text(
                    text = "Redimensione as suas imagens",
                    fontSize = 12.sp,
                    color = SizeProPalette.TextSecondary
                )

                Text(
                    text = "com qualidade profissional!",
                    fontSize = 12.sp,
                    color = SizeProPalette.TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // IMAGEM DE EXEMPLO

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = BorderGradient,
                    shape = RoundedCornerShape(22.dp)
                ),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = SizeProPalette.Surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Box {
                Image(
                    painter = painterResource(
                        id = R.drawable.sample_photo
                    ),
                    contentDescription = "Fotografia de exemplo do Size Pro",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xE6111116))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = SizeProPalette.Orange,
                        modifier = Modifier.size(17.dp)
                    )

                    Text(
                        text = " Exemplo offline",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // TÍTULO E DESCRIÇÃO

        Text(
            text = "Escolha uma fotografia",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = "Seleccione uma imagem JPG, PNG ou WebP do seu dispositivo.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(23.dp))

        // BOTÃO PRINCIPAL

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(ActionGradient)
                .clickable(role = Role.Button) {
                    photoPicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
                .semantics {
                    contentDescription = "Escolher uma imagem do dispositivo"
                }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(27.dp)
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = "Escolher imagem",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "›",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.height(17.dp))

        // IMAGEM DE EXEMPLO OFFLINE

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(SizeProPalette.Surface)
                .border(
                    width = 1.dp,
                    brush = BorderGradient,
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable(role = Role.Button) {
                    val sampleUri = Uri.parse(
                        "android.resource://${context.packageName}/${R.drawable.sample_photo}"
                    )
                    onImageSelected(sampleUri)
                }
                .semantics {
                    contentDescription = "Usar imagem de exemplo incluída na aplicação"
                }
                .padding(horizontal = 19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = SizeProPalette.Violet,
                modifier = Modifier.size(25.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = "Usar imagem de exemplo",
                    color = SizeProPalette.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Incluída na aplicação",
                    color = SizeProPalette.TextSecondary,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "›",
                color = SizeProPalette.Blue,
                fontSize = 30.sp
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // BENEFÍCIOS

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FeatureItem(
                icon = Icons.Default.Lock,
                label = "Seguro",
                color = SizeProPalette.Green
            )

            FeatureItem(
                icon = Icons.Default.Bolt,
                label = "Rápido",
                color = SizeProPalette.Orange
            )

            FeatureItem(
                icon = Icons.Default.Star,
                label = "Qualidade",
                color = SizeProPalette.Pink
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PRIVACIDADE

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(SizeProPalette.Green)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "A sua imagem permanece no seu dispositivo.",
                fontSize = 12.sp,
                color = SizeProPalette.Green,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.14f))
                .border(
                    width = 1.dp,
                    color = color.copy(alpha = 0.30f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(23.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = SizeProPalette.TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}
