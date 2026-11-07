package com.pixelresize.pro

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onActionClick: () -> Unit
) {
    val keys = listOf(
        listOf("7", "8", "9"),
        listOf("4", "5", "6"),
        listOf("1", "2", "3")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keys.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { key ->
                    KeyItem(text = key, modifier = Modifier.weight(1f).clickable { onNumberClick(key) })
                }
                if (row[0] == "7") {
                    Box(modifier = Modifier.weight(1f).height(55.dp).clip(RoundedCornerShape(12.dp)).background(ColorSuperficie).clickable { onDeleteClick() }, contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Backspace, contentDescription = null, color = ColorRosa)
                    }
                }
                if (row[0] == "4") {
                    Box(modifier = Modifier.weight(1f).height(55.dp).clip(RoundedCornerShape(12.dp)).background(ColorSuperficie), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.SwapVert, contentDescription = null, color = ColorAzul)
                    }
                }
                if (row[0] == "1") {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeyItem(text = "0", modifier = Modifier.weight(3f).clickable { onNumberClick("0") })
            Button(
                onClick = onActionClick,
                modifier = Modifier.weight(1f).height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorRosa),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Resize", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun KeyItem(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .height(55.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(ColorSuperficie),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
