package com.example.baydesigns

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.baydesigns.ui.theme.BayDesignsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BayDesignsTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    Column(){
        Text("HI")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BayDesignsTheme {
        Greeting()
    }
}