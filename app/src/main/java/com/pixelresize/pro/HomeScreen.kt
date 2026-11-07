package com.pixelresize.pro

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onImageSelected: (Uri) -> Unit,
    onUseSample: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { onImageSelected(it) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFundo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = BoxArrangement.SpaceBetween
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ColorRosa),
                contentAlignment = Alignment.Center
            ) {
                Text("SP", color = Color.White, fontSize = 24.bold, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Size Pro", color = ColorRosa, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("Redimensione as suas imagens com qualidade profissional!", color = Color.Gray, fontSize = 14.sp)
            }
        }

        // Preview Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, ColorAzul, RoundedCornerShape(24.dp))
        ) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Placeholder para imagem Fuji
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Image, contentDescription = null, color = ColorVerde, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Exemplo offline", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        // Section Center
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Escolha uma fotografia", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Seleccione uma imagem JPG, PNG ou WebP do seu dispositivo.",
                color = Color.Gray, fontSize = 14.sp, textAlign = TextAlign.Center
            )
        }

        // Actions
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Main Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(ColorLaranja, ColorRosa)))
                    .clickable { launcher.launch("image/*") }
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Image, contentDescription = null, color = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Escolher imagem", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, color = Color.White)
            }

            // Secondary Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ColorSuperficie)
                    .border(1.dp, ColorAzul, RoundedCornerShape(20.dp))
                    .clickable { onUseSample() }
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, color = ColorLaranja)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Usar imagem de exemplo", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text("Incluída na aplicação", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, color = ColorAzul)
            }
        }

        // Lower Badges
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(45.dp).clip(CircleShape).background(ColorSuperficie).border(1.dp, ColorVerde, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Lock, contentDescription = null, color = ColorVerde, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Seguro", color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(45.dp).clip(CircleShape).background(ColorSuperficie).border(1.dp, ColorLaranja, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.FlashOn, contentDescription = null, color = ColorLaranja, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Rápido", color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(45.dp).clip(CircleShape).background(ColorSuperficie).border(1.dp, ColorRosa, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Star, contentDescription = null, color = ColorRosa, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Qualidade", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
