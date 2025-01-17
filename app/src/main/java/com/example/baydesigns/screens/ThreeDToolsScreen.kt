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
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
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
import androidx.navigation.compose.rememberNavController
import com.example.baydesigns.SharedViewModel

data class ToolItem(
    val id: Int,
    val name: String,
    val type: String, // "3D Model" or "Wallpaper"
    val resource: String // Path to the GLB file or wallpaper
)

@Composable
fun ThreeDToolsScreen(navBackStackEntry: NavBackStackEntry) {
    val navController = rememberNavController()
    val viewModel: SharedViewModel = viewModel()

    val toolsList = listOf(
        ToolItem(1, "Plane", "3D Model", "models/plane.glb"),
        ToolItem(2, "Sofa", "Wallpaper", "models/sofa.glb")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 3D Models Section
        item {
            Text(
                text = "3D Models",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(toolsList.filter { it.type == "3D Model" }) { tool ->
            ToolItemCard(tool) {
                viewModel.selectTool(tool.resource) // Update the selected tool in the ViewModel
                navController.popBackStack() // Navigate back to HomeScreen
            }
        }

        // Wallpapers Section
        item {
            Text(
                text = "Wallpapers",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(toolsList.filter { it.type == "Wallpaper" }) { tool ->
            ToolItemCard(tool) {
                viewModel.selectTool(tool.resource) // Update the selected tool in the ViewModel
                navController.popBackStack() // Navigate back to HomeScreen
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