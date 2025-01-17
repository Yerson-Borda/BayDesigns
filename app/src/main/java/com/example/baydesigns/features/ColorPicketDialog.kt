package com.example.baydesigns.features

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPickerDialog(
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    var red by remember { mutableFloatStateOf(0f) }
    var green by remember { mutableFloatStateOf(0f) }
    var blue by remember { mutableFloatStateOf(0f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onColorSelected(Color(red / 255f, green / 255f, blue / 255f))
                onDismiss()
            }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Select RGB Color") },
        text = {
            Column {
                Text("Adjust RGB Values")
                Spacer(modifier = Modifier.height(16.dp))

                SliderWithLabel("Red", red) { red = it }
                SliderWithLabel("Green", green) { green = it }
                SliderWithLabel("Blue", blue) { blue = it }
            }
        }
    )
}

@Composable
fun SliderWithLabel(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Text("$label: ${value.toInt()}")
        Slider(
            value = value,
            onValueChange = { onValueChange(it * 255) },
            valueRange = 0f..1f
        )
    }
}
