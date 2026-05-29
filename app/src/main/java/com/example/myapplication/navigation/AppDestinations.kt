package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * The set of top-level destinations (tabs) shown by [com.example.myapplication.MyApp].
 *
 * An `enum class` is used (instead of a sealed class or list of objects) because:
 *  - The set of tabs is fixed and known at compile time.
 *  - `entries` gives us a free, ordered list to iterate in the navigation bar.
 *  - The compiler can warn us if a `when` over destinations is non-exhaustive,
 *    which means adding a new tab forces us to handle it in every screen switch.
 *
 * Each entry carries the display label and the icon, so the navigation UI does not
 * need to hard-code that information per destination.
 */
enum class AppDestinations(
  val label: String,
  val icon: ImageVector
) {
  HOME("Home", Icons.Default.Home),
  FAVORITES("Favorites", Icons.Default.Favorite),
  PROFILE("Profile", Icons.Default.AccountBox)
}
