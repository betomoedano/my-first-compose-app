package com.example.myapplication

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.navigation.AppDestinations
import com.example.myapplication.ui.components.FabMenu
import com.example.myapplication.ui.screens.FavoritesScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.ProfileScreen

/**
 * The root composable of the app.
 *
 * Responsibilities (and intentionally only these):
 *  1. Hold the **currently selected tab** as state.
 *  2. Render the navigation chrome (`NavigationSuiteScaffold` adapts between
 *     bottom bar / rail / drawer based on window size).
 *  3. Pick which screen to show based on the selected tab.
 *  4. Overlay the FAB menu on top of the screen content.
 *
 * Everything else is delegated to a dedicated file:
 *  - Tab definitions → [AppDestinations]
 *  - Screen content → files under `ui/screens/`
 *  - FAB → [FabMenu]
 *
 * Keeping this file thin is the whole point of the folder structure.
 * If `MyApp` starts handling rendering details, the architecture has drifted.
 *
 * @param onClick callback fired whenever any FAB menu action is tapped.
 *   Hoisted up to [MainActivity] so non-Compose code (e.g. analytics, logging)
 *   can react without `MyApp` needing to know about it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(onClick: () -> Unit = {}) {
  // `rememberSaveable` keeps the selected tab across rotation. We pass a key
  // ("currentDestination") to be explicit, though Compose generates one
  // automatically based on call-site position.
  //
  // A `mutableStateOf(...)` wrapped in `by` lets us read/write `currentDestination`
  // like a normal variable. Under the hood, reads subscribe the current
  // composition to changes — so when this value changes, only the parts that
  // read it recompose.
  var currentDestination by rememberSaveable {
    mutableStateOf(AppDestinations.HOME)
  }

  NavigationSuiteScaffold(
    navigationSuiteItems = {
      // Iterate over every tab and produce a navigation item for it.
      // `entries` is the Kotlin 1.9+ replacement for `values()` on enums.
      AppDestinations.entries.forEach { destination ->
        item(
          icon = {
            Icon(destination.icon, contentDescription = destination.label)
          },
          label = { Text(destination.label) },
          selected = destination == currentDestination,
          onClick = { currentDestination = destination }
        )
      }
    }
  ) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
      // We use a Box (not Column) here because we want the FAB to overlay
      // the screen content, not sit below it. Children of a Box are stacked
      // in Z order in the order they're declared — screen first, FAB on top.
      Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        // Switch screens based on the current tab.
        // Because `AppDestinations` is an enum, the compiler verifies this
        // `when` is exhaustive — adding a new tab causes a build error here,
        // which is exactly the safety we want.
        when (currentDestination) {
          AppDestinations.HOME -> HomeScreen()
          AppDestinations.FAVORITES -> FavoritesScreen()
          AppDestinations.PROFILE -> ProfileScreen()
        }

        // Overlay the FAB menu in the bottom-right corner.
        // `Modifier.align` is only available inside a BoxScope, which is why
        // it works here but wouldn't inside a Column.
        FabMenu(
          modifier = Modifier.align(Alignment.BottomEnd),
          onAction = onClick
        )
      }
    }
  }
}
