package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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

  // Shared pool of shapes we randomly assign to each avatar.
  val shapePool = rememberShapePool()

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
//    topBar = { MyTopBar(userList) },
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
      // Split the flat user list into groups of 5. Each group renders as one
      // connected, segmented unit. Keyed on `userList` so it only recomputes
      // when the data actually changes.
      val groups = remember(userList) { userList.chunked(5) }

      LazyColumn(
        modifier = Modifier
          .padding(innerPadding)
          // Tinted page tone so the near-white item cards lift off it,
          // matching the Pixel "tinted background, white cards" look.
          .background(MaterialTheme.colorScheme.surfaceContainerHigh)
          .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp), // gap BETWEEN groups
      ) {
        items(groups.size) { groupIndex ->
          val group = groups[groupIndex]

          Column(
            // Small 2dp "segmented" gaps separate the filled items inside a group.
            verticalArrangement = Arrangement.spacedBy(2.dp)
          ) {
            group.forEachIndexed { indexInGroup, user ->
              // Random avatar shape, kept stable per user via the email key.
              val avatarShape = remember(user.email) { shapePool.random() }

              UserListItem(
                user = user,
                avatarShape = avatarShape.toShape(),
                containerShape = listItemShape(indexInGroup, group.size),
              )
            }
          }
        }
      }
    }
  }
}

/**
 * A single filled list item: an avatar plus the user's name and email.
 *
 * The container is clipped to [containerShape] *before* the background is
 * drawn, so the fill follows the rounded corners. Padding comes last so it
 * insets the content, not the background.
 *
 * @param containerShape the rounded-corner shape for this item's position in
 *   its group (large outer corners on the group's ends, small in the middle).
 */
@Composable
private fun UserListItem(
  user: User,
  avatarShape: Shape,
  containerShape: Shape,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(containerShape)
      // Lowest container tone = near white, so cards pop against the page.
      .background(MaterialTheme.colorScheme.surfaceContainerLowest)
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    BetoPic(
      url = user.photo,
      shape = avatarShape,
      modifier = Modifier.size(56.dp)
    )
    Column(modifier = Modifier.weight(1f)) {
      Text(
        user.name,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
      )
      Text(
        user.email,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

/**
 * Corner shape for a filled list item based on its position within a group.
 * The group's outer edges round off (16dp) while the inner edges between items
 * stay nearly square (4dp), so a group of items reads as one connected block.
 * A single-item group rounds all four corners.
 */
private fun listItemShape(indexInGroup: Int, groupSize: Int): RoundedCornerShape {
  val large = 16.dp
  val small = 4.dp
  val top = if (indexInGroup == 0) large else small
  val bottom = if (indexInGroup == groupSize - 1) large else small
  return RoundedCornerShape(topStart = top, topEnd = top, bottomStart = bottom, bottomEnd = bottom)
}

/**
 * A horizontally scrolling strip of user avatars, each clipped to a random
 * shape — a compact "stories"-style preview of the same users shown below.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyTopBar(users: List<User>) {
  val shapePool = rememberShapePool()

  LazyRow(
    modifier = Modifier.height(100.dp).background(MaterialTheme.colorScheme.surfaceContainerLowest),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(horizontal = 8.dp)
  ) {
    items(users.size) { index ->
      val user = users[index]
      val userShape = remember(index) { shapePool.random() }

      BetoPic(
        url = user.photo,
        shape = userShape.toShape(),
        modifier = Modifier.size(64.dp)
      )
    }
  }
}

/**
 * The set of shapes we randomly pick from for avatars. Wrapped in `remember`
 * so the list is built once and shared by every screen that needs it.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun rememberShapePool() = remember {
  listOf(
    MaterialShapes.Ghostish,
    MaterialShapes.Cookie6Sided,
    MaterialShapes.Sunny,
    MaterialShapes.Arrow,
    MaterialShapes.Square,
    MaterialShapes.Gem,
  )
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

