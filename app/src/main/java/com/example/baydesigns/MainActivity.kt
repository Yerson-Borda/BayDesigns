package com.example.baydesigns

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.baydesigns.features.CameraUtils
import com.example.baydesigns.features.ColorPickerDialog
import com.example.baydesigns.features.captureBitmap
import com.example.baydesigns.ui.theme.BayDesignsTheme
import com.google.ar.core.Config
import io.github.sceneview.SceneView
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BayDesignsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(modifier = Modifier.fillMaxSize()){
                        val currentModel = remember {
                            mutableStateOf("sofa")
                        }
                        ARScreen(currentModel.value)
                    }
                }
            }
        }
    }
}

@Composable
fun ARScreen(model: String) {
    val nodes = remember { mutableListOf<ArNode>() }
    val modelNode = remember { mutableStateOf<ArModelNode?>(null) }
    val arSceneView = remember { mutableStateOf<SceneView?>(null) }
    val placeModelButton = remember { mutableStateOf(false) }
    val showColorPicker = remember { mutableStateOf(false) }
    val selectedColor = remember { mutableStateOf(Color.White) }

    val context = LocalContext.current

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
                    ) {}
                    onAnchorChanged = { placeModelButton.value = !isAnchored }
                    onHitResult = { node, _ -> placeModelButton.value = node.isTracking }
                }
                nodes.add(modelNode.value!!)
            },
            onSessionCreate = { planeRenderer.isVisible = false }
        )

        if (placeModelButton.value) {
            Button(onClick = { modelNode.value?.anchor() }, modifier = Modifier.align(Alignment.Center)) {
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
                val rgbCircleIcon: Painter = painterResource(id = R.drawable.rgb_circle)

                Image(
                    painter = rgbCircleIcon,
                    contentDescription = "Change color icon",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { showColorPicker.value = true }
                )

                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Take a photo of your design",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            // Access ARSceneView directly from the captured reference
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

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "More options",
                    tint = Color.Black,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        if (showColorPicker.value) {
            ColorPickerDialog(
                onColorSelected = { selectedColor.value = it },
                onDismiss = { showColorPicker.value = false }
            )
        }
    }
}