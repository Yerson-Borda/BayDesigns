package com.example.baydesigns.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import com.example.baydesigns.SharedViewModel
import com.example.baydesigns.features.CameraPreview

@Composable
fun ColorRecognitionScreen(navController: NavController, viewModel: SharedViewModel) {
    val dominantColors = remember { mutableStateOf<List<Color>>(emptyList()) }
    val selectedColor = remember { mutableStateOf<Color?>(null) }

    CameraPreview { bitmap ->
        val palette = Palette.from(bitmap).generate()
        val colors = listOf(
            palette.getDominantColor(Color.Black.toArgb()),
            palette.getVibrantColor(Color.Black.toArgb()),
            palette.getMutedColor(Color.Black.toArgb())
        ).map { Color(it) }

        dominantColors.value = colors
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dominantColors.value.forEach { color ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color)
                    .clickable {
                        selectedColor.value = color
                    }
            )
        }
    }

    // Pass the selected color to ARScreen
    ARScreen(
        model = viewModel.selectedTool.value ?: "models/plane.glb",
        wallColor = selectedColor.value ?: Color.White
    )
}