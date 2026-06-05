package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.components.BetoPic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun ProfileScreen() {
  var userList by remember { mutableStateOf<List<User>>(emptyList()) }
  var isLoading by remember { mutableStateOf(true) }

  // The pool of shapes we randomly assign to each avatar. Held in `remember`
  // so the list isn't rebuilt on every recomposition.
  val shapePool = remember {
    listOf(
      MaterialShapes.Ghostish,
      MaterialShapes.Cookie6Sided,
      MaterialShapes.Sunny,
      MaterialShapes.Arrow,
      MaterialShapes.Square,
      MaterialShapes.Gem,
    )
  }

  LaunchedEffect(Unit) {
    withContext(Dispatchers.IO) {
      try {
        val response = URL("https://randomuser.me/api/?results=50").readText()
        val json = JSONObject(response)
        val results = json.getJSONArray("results")
        val fetchedUsers = mutableListOf<User>()
        for (i in 0 until results.length()) {
          val userJson = results.getJSONObject(i)
          val nameObj = userJson.getJSONObject("name")
          val name = "${nameObj.getString("first")} ${nameObj.getString("last")}"
          val email = userJson.getString("email")
          val photo = userJson.getJSONObject("picture").getString("large")
          fetchedUsers.add(User(name, email, photo))
        }
        userList = fetchedUsers
      } catch (e: Exception) {
        Log.e("ProfileScreen", "Error fetching users", e)
      } finally {
        isLoading = false
      }
    }
  }

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
    if (isLoading) {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text("Loading...")
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        items(userList.size) { index ->
          val user = userList[index]
          // Pick a random shape once per row and keep it stable across
          // recompositions by keying the `remember` on the index.
          val userShape = remember(index) { shapePool.random() }

          Row(
            modifier = Modifier
              .padding(horizontal = 16.dp)
              .width(350.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            BetoPic(
              url = user.photo,
              shape = userShape.toShape(),
              modifier = Modifier.size(80.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
              Text(user.name, style = MaterialTheme.typography.titleMedium)
              Text(user.email, style = MaterialTheme.typography.bodySmall)
            }
          }
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

