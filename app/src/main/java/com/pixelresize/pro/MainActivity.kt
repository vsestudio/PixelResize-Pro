package com.pixelresize.pro

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.pixelresize.pro.data.ImageResult
import com.pixelresize.pro.image.ImageSharing

private enum class AppScreen {
    HOME,
    EDITOR,
    RESULT
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        setContent {
            PixelResizeTheme {
                PixelResizeApp()
            }
        }
    }
}

@Composable
fun PixelResizeApp() {

    val context = LocalContext.current

    var currentScreen by remember {
        mutableStateOf(AppScreen.HOME)
    }

    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }

    var imageResult by remember {
        mutableStateOf<ImageResult?>(null)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        color = MaterialTheme.colorScheme.background
    ) {

        when (currentScreen) {

            AppScreen.HOME -> {

                HomeScreen(
                    onImageSelected = { uri ->

                        selectedImage = uri
                        imageResult = null

                        currentScreen = AppScreen.EDITOR
                    }
                )
            }

            AppScreen.EDITOR -> {

                val imageUri = selectedImage

                if (imageUri != null) {

                    ResizeScreen(
                        imageUri = imageUri,

                        onBack = {
                            currentScreen = AppScreen.HOME
                        },

                        onImageProcessed = {
                                uri,
                                width,
                                height,
                                format,
                                fileSize ->

                            imageResult = ImageResult(
                                uri = uri,
                                width = width,
                                height = height,
                                fileSizeBytes = fileSize,
                                format = format
                            )

                            currentScreen = AppScreen.RESULT
                        }
                    )

                } else {

                    currentScreen = AppScreen.HOME
                }
            }

            AppScreen.RESULT -> {

                val result = imageResult

                if (result != null) {

                    ResultScreen(
                        result = result,

                        onShare = {
                            ImageSharing.shareImage(
                                context = context,
                                imageUri = result.uri,
                                format = result.format
                            )
                        },

                        onResizeAgain = {
                            currentScreen = AppScreen.EDITOR
                        },

                        onUploadAnother = {
                            selectedImage = null
                            imageResult = null

                            currentScreen = AppScreen.HOME
                        }
                    )

                } else {

                    currentScreen = AppScreen.HOME
                }
            }
        }
    }
}
