package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * The Favorites tab — currently a placeholder.
 *
 * Each tab gets its own dedicated `*Screen` composable, even when the content
 * is trivial. This way, when we want to add real favorites later, the structure
 * is already in place and we only have to fill in this one file instead of
 * untangling a generic placeholder shared by multiple tabs.
 *
 * `Box(fillMaxSize) + contentAlignment = Center` is the standard pattern for
 * centering a single child in the available space.
 */
@Composable
fun FavoritesScreen() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Text("Favorites", style = MaterialTheme.typography.headlineMedium)
  }
}
