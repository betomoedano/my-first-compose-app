package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.components.BetoPic

/**
 * The Profile tab — currently a placeholder.
 *
 * Same pattern as [FavoritesScreen]: dedicated file per tab so each can grow
 * independently without forcing churn on the others.
 */
@Preview
@Composable
fun ProfileScreen() {

  Scaffold(
    topBar = { MyTopBar() },
//    bottomBar = {
//      BottomAppBar() {
//        MyBottomBar()
//      }
//    },
    floatingActionButton = {
      FloatingActionButton({ Log.d("hello", "hello logs") }) {
        Icon(Icons.Default.Star, "Star")
      }
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .padding(innerPadding)
        .border(BorderStroke(1.dp, Color.Red)),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
      }
    }
  }
}

@Composable
fun MyTopBar() {
  LazyRow(
    modifier = Modifier
      .height(100.dp)
      .border(BorderStroke(1.dp, Color.Green)),
  ) {
    // Add 5 items
    items(50) { index ->
      Text(text = "Item: $index")
    }
  }
}

@Composable
fun MyBottomBar() {
  Text("My Bottom Bar")
}


data class User(
  val name: String,
  val email: String,
  val photo: String?
)

val users = listOf<User>(
  User("John Doe", "john.mclean@examplepetstore.com", null),
  User("Beto", "william.henry.moody@my-own-personal-domain.com", "https://github.com/betomoedano.png"),
  User("Cess Doe", "john.c.calhoun@examplepetstore.com", null),
)
