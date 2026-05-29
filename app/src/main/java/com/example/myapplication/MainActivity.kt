package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MyApp(onClick = { onClick() })
      }
    }
  }

  private fun onClick() {
    Log.d("[Beto Logs]", "Beto was here!")
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(onClick: () -> Unit = {}) {
  // Define the state for the current destination
  var currentDestination by remember { mutableStateOf(AppDestinations.HOME) }
  var presses by remember { mutableIntStateOf(0) }

  NavigationSuiteScaffold(
    navigationSuiteItems = {
      AppDestinations.entries.forEach { destination ->
        item(
          icon = {
            Icon(
              destination.icon,
              contentDescription = destination.label
            )
          },
          label = { Text(destination.label) },
          selected = destination == currentDestination,
          onClick = { currentDestination = destination }
        )
      }
    }
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      floatingActionButton = {
        FloatingActionButton(onClick = {
          presses++
          onClick()
        }) {
          Icon(Icons.Default.Add, contentDescription = "Add")
        }
      }
    ) { innerPadding ->
      Column(modifier = Modifier.padding(innerPadding)) {
        BetoPic()
        CarouselExample()
      }
    }
  }
}


@Composable
fun BetoPic(modifier: Modifier = Modifier) {
  AsyncImage(
    model = "https://github.com/betomoedano.png",
    contentDescription = "Profile picture",
    contentScale = ContentScale.Crop,
    modifier = modifier
      .size(120.dp)
      .clip(CircleShape)
      .border(2.dp, Color.White, CircleShape)
  )
}
