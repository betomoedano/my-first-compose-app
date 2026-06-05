package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
        .fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      items(users.size) { index ->
        val user = users[index]
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          BetoPic(url = user.photo)
          Text(user.name, style = MaterialTheme.typography.headlineMedium)
          Text(user.email, style = MaterialTheme.typography.bodySmall)
        }
      }
    }
  }
}

@Composable
fun MyTopBar() {
  LazyRow(
    modifier = Modifier
      .height(100.dp)
  ) {
    // Add 50 items to match the users
    items(50) { index ->
      Text(text = "Item: $index ", modifier = Modifier.padding(8.dp))
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

val users = List(50) { index ->
  User(
    name = "User $index",
    email = "user$index@example.com",
    photo = if (index % 3 == 0) "https://github.com/betomoedano.png" else null
  )
}

