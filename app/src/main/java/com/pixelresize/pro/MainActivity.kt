package com.pixelresize.pro

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.pixelresize.pro.data.ImageResult

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PixelResizeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ColorFundo
                ) {
                    var currentScreen by remember { mutableStateOf("home") }
                    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
                    var imageResult by remember { mutableStateOf<ImageResult?>(null) }

                    when (currentScreen) {
                        "home" -> HomeScreen(
                            onImageSelected = { uri ->
                                selectedImageUri = uri
                                currentScreen = "resize"
                            },
                            onUseSample = {
                                selectedImageUri = null // nulo indica imagem de amostra interna
                                currentScreen = "resize"
                            }
                        )
                        "resize" -> ResizeScreen(
                            imageUri = selectedImageUri,
                            onResizeCompleted = { result ->
                                imageResult = result
                                currentScreen = "result"
                            },
                            onBack = { currentScreen = "home" }
                        )
                        "result" -> ResultScreen(
                            result = imageResult,
                            onResizeAgain = { currentScreen = "resize" },
                            onUploadAnother = { currentScreen = "home" },
                            onBack = { currentScreen = "resize" }
                        )
                    }
                }
            }
        }
    }
}
