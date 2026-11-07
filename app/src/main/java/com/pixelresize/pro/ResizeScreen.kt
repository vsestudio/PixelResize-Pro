package com.pixelresize.pro

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
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
fun ResizeScreen(
    imageUri: Uri?,
    onResizeCompleted: (ImageResult) -> Unit,
    onBack: () -> Unit
) {
    var widthText by remember { mutableStateOf("547") }
    var heightText by remember { mutableStateOf("365") }
    var activeField by remember { mutableStateOf("width") } // "width" ou "height"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFundo)
            .padding(16.dp)
    ) {
        // App Bar
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

        // Image Card Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp)
                .background(ColorSuperficie, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = ColorSuperficie),
                        modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Change", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Original: 547 × 365 px · 162 KB", color = Color.Gray, fontSize = 13.sp)
            }
        }

        // Inputs Area
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Width
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(ColorSuperficie, RoundedCornerShape(12.dp))
                    .border(
                        width = if (activeField == "width") 2.dp else 1.dp,
                        color = if (activeField == "width") ColorAzul else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { activeField = "width" }
                    .padding(12.dp)
            ) {
                Text("Width (px)", color = ColorAzul, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(widthText, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            // Ratio Lock
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ColorRosa),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, color = Color.White, modifier = Modifier.size(18.dp))
            }

            // Height
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(ColorSuperficie, RoundedCornerShape(12.dp))
                    .border(
                        width = if (activeField == "height") 2.dp else 1.dp,
                        color = if (activeField == "height") ColorLaranja else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { activeField = "height" }
                    .padding(12.dp)
            ) {
                Text("Height (px)", color = ColorLaranja, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(heightText, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }

        Text("Output size", color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))

        // Custom Numeric Keypad Component
        NumericKeypad(
            onNumberClick = { num ->
                if (activeField == "width") widthText += num else heightText += num
            },
            onDeleteClick = {
                if (activeField == "width") {
                    if (widthText.isNotEmpty()) widthText = widthText.dropLast(1)
                } else {
                    if (heightText.isNotEmpty()) heightText = heightText.dropLast(1)
                }
            },
            onActionClick = {
                onResizeCompleted(
                    ImageResult(
                        width = widthText.toIntOrNull() ?: 1700,
                        height = heightText.toIntOrNull() ?: 1132,
                        sizeKb = 265,
                        format = "JPG"
                    )
                )
            }
        )
    }
}
