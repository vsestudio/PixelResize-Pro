package com.pixelresize.pro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelresize.pro.data.ImageResult

@Composable
fun ResultScreen(
    result: ImageResult?,
    onResizeAgain: () -> Unit,
    onUploadAnother: () -> Unit,
    onBack: () -> Unit
) {
    var selectedFormat by remember { mutableStateOf("JPG") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFundo)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, color = Color.White)
            }
            Text("Resize", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = null, color = Color.White)
            }
        }

        // Image Result Wrapper
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp)
                .background(ColorSuperficie, RoundedCornerShape(16.dp))
                .border(2.dp, ColorVerde, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = null,
                    modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Result: ${result?.width ?: 1700} × ${result?.height ?: 1132} px · ${result?.sizeKb ?: 265} KB",
                    color = ColorVerde,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Dimension box values
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f).background(ColorSuperficie, RoundedCornerShape(12.dp)).padding(12.dp)) {
                Text("Width (px)", color = Color.Gray, fontSize = 12.sp)
                Text("${result?.width ?: 1700}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(1f).background(ColorSuperficie, RoundedCornerShape(12.dp)).padding(12.dp)) {
                Text("Height (px)", color = Color.Gray, fontSize = 12.sp)
                Text("${result?.height ?: 1132}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Info mid status
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .background(ColorSuperficie, RoundedCornerShape(12.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "New size: ${result?.width ?: 1700} × ${result?.height ?: 1132} px · ${result?.sizeKb ?: 265} KB",
                color = ColorVerde, fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
        }

        // Formats Selectors
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("JPG", "PNG", "WEBP").forEach { format ->
                val isSelected = selectedFormat == format
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(45.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ColorSuperficie)
                        .border(1.5.dp, if (isSelected) ColorVerde else Color.Transparent, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(format, color = if (isSelected) ColorVerde else Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Primary Outputs
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {},
                modifier = Modifier.weight(1f).height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorVerde),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, color = ColorFundo)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Download", color = ColorFundo, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {},
                modifier = Modifier.weight(1f).height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorSuperficie),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, ColorVerde)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, color = ColorVerde)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share", color = ColorVerde, fontWeight = FontWeight.Bold)
            }
        }

        // Footers
        Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onResizeAgain,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = ColorSuperficie)
            ) {
                Text("Resize again", color = Color.White)
            }
            Button(
                onClick = onUploadAnother,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = ColorSuperficie)
            ) {
                Text("Upload another", color = Color.White)
            }
        }
    }
}
