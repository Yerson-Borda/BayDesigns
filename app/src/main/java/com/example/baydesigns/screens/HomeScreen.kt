package com.example.baydesigns.screens
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.baydesigns.R
import com.example.baydesigns.SharedViewModel
import com.example.baydesigns.features.CameraUtils
import com.example.baydesigns.features.captureBitmap
import com.google.ar.core.Config
import io.github.sceneview.SceneView
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode

@Composable
fun HomeScreen(navController: NavController, viewModel: SharedViewModel) {
    val selectedTool by viewModel.selectedTool.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        ARScreen(model = selectedTool ?: "models/plane.glb")


        var expanded by remember { mutableStateOf(false) }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options"
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 75.dp, end = 16.dp)
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        navController.navigate("3dTools")
                    },
                    text = { Text("3D Items") }
                )
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        // Handle Option 2 click
                    },
                    text = { Text("AI") }
                )
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        // Handle Option 3 click
                    },
                    text = { Text("Saved Designs") }
                )
            }
        }
    }
}

@Composable
fun ARScreen(
    model: String,
    wallColor: Color = Color.White // Add wallColor parameter with a default value
) {
    val nodes = remember { mutableListOf<ArNode>() }
    val modelNode = remember { mutableStateOf<ArModelNode?>(null) }
    val placeModelButton = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val arSceneView = remember { mutableStateOf<SceneView?>(null) }
    val showColorPalette = remember { mutableStateOf(false) } // State for color palette visibility
    val selectedColor = remember { mutableStateOf<Color?>(null) } // State for selected color

    Box(modifier = Modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = { arSceneViewInstance ->
                // Capture the ARSceneView reference
                arSceneView.value = arSceneViewInstance

                arSceneViewInstance.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneViewInstance.planeRenderer.isShadowReceiver = false

                modelNode.value = ArModelNode(arSceneViewInstance.engine, PlacementMode.INSTANT).apply {
                    loadModelGlbAsync(
                        glbFileLocation = "models/${model}.glb",
                        scaleToUnits = 0.8f
                    ) {
                        // Apply the selected color to the model's material
                        selectedColor.value?.let { color ->
                            applyMaterialColor(color)
                        }
                    }
                    onAnchorChanged = { placeModelButton.value = !isAnchored }
                    onHitResult = { node, _ -> placeModelButton.value = node.isTracking }
                }
                nodes.add(modelNode.value!!)
            },
            onSessionCreate = {
                planeRenderer.isVisible = false
            }
        )

        if (placeModelButton.value) {
            Button(
                onClick = { modelNode.value?.anchor() },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "Place It")
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xFF878080))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF878080)),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // RGB Circle Icon to Show Color Palette
                val rgbCircleIcon: Painter = painterResource(id = R.drawable.rgb_circle)
                Image(
                    painter = rgbCircleIcon,
                    contentDescription = "Change color icon",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            showColorPalette.value = !showColorPalette.value // Toggle color palette visibility
                        }
                )

                // Camera Icon
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Take a photo of your design",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            arSceneView.value?.let { sceneView ->
                                sceneView.captureBitmap { bitmap ->
                                    if (bitmap != null) {
                                        val success = CameraUtils.saveImageToGallery(context, bitmap)
                                        if (success) {
                                            Toast.makeText(context, "Photo saved to gallery!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Failed to save photo.", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "Failed to capture image.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } ?: run {
                                Toast.makeText(context, "AR Scene is not ready.", Toast.LENGTH_SHORT).show()
                            }
                        }
                )

                // More Options Icon
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "More options",
                    tint = Color.Black,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Show Color Palette if Visible
        if (showColorPalette.value) {
            ColorPalette(
                onColorSelected = { color ->
                    selectedColor.value = color
                    showColorPalette.value = false // Hide the palette after selection
                    modelNode.value?.applyMaterialColor(color) // Update the wall color
                },
                onDismiss = { showColorPalette.value = false }
            )
        }
    }

    LaunchedEffect(model, selectedColor.value) { // Add selectedColor as a key to recompose when it changes
        modelNode.value?.loadModelGlbAsync(
            glbFileLocation = model,
            scaleToUnits = 0.8f
        ) {
            // Apply the selected color to the model's material
            selectedColor.value?.let { color ->
                modelNode.value?.applyMaterialColor(color)
            }
        }
    }
}

@Composable
fun ColorPalette(
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Cyan,
        Color.Magenta,
        Color.Gray,
        Color.White,
        Color.Black
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.9f))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Select a Color",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color)
                            .clickable {
                                onColorSelected(color)
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Close")
            }
        }
    }
}

fun ArModelNode.applyMaterialColor(color: Color) {
    this.modelInstance?.getMaterialInstances()?.forEach { materialInstance ->
        materialInstance.setParameter("baseColorFactor", color.toArgb())
    }
}