package com.example.baydesigns.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baydesigns.SharedViewModel

data class ToolItem(
    val id: Int,
    val name: String,
    val type: String, // "3D Model" or "Wallpaper"
    val resource: String // Path to the GLB file or wallpaper
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreeDToolsScreen(navController: NavController, viewModel: SharedViewModel) {
    val toolsList = listOf(
        ToolItem(1, "Plane", "Wallpaper", "models/plane.glb"),
        ToolItem(2, "Sofa", "Furniture", "models/sofa.glb"),
        ToolItem(3, "table", "Furniture", "models/table.glb"),
        ToolItem(4, "stove", "Furniture", "models/stove.glb")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        androidx.compose.material3.TopAppBar(
            title = { Text("3D Items") },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(8.dp)
                )
            },
            modifier = Modifier.background(Color.White)
        )

        // Content below the Top Bar
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp) // Adjust for TopAppBar height
        ) {
            // 3D Models Section
            item {
                Text(
                    text = "Wallpapers",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(toolsList.filter { it.type == "Wallpaper" }) { tool ->
                ToolItemCard(tool) {
                    viewModel.selectTool(tool.resource)
                    navController.popBackStack()
                }
            }

            // Wallpapers Section
            item {
                Text(
                    text = "Furniture",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(toolsList.filter { it.type == "Furniture" }) { tool ->
                ToolItemCard(tool) {
                    viewModel.selectTool(tool.resource)
                    navController.popBackStack()
                }
            }
        }
    }
}


@Composable
fun ToolItemCard(tool: ToolItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() } // Use Modifier.clickable for click handling
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tool.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (tool.type == "3D Model") Icons.Default.Dashboard else Icons.Default.Image,
                contentDescription = tool.type
            )
        }
    }
}