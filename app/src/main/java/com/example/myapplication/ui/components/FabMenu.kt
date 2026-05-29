package com.example.myapplication.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter

/**
 * An expanding Floating Action Button menu, built with the official Material 3
 * `FloatingActionButtonMenu` + `ToggleFloatingActionButton` + `FloatingActionButtonMenuItem` APIs.
 *
 * Behavior:
 *  - Tapping the main FAB toggles between a `+` (collapsed) and `×` (expanded) icon.
 *  - When expanded, the menu items (Edit, Favorite, Share) animate in above it.
 *  - Tapping a menu item collapses the menu and fires [onAction].
 *  - Pressing the system back button collapses the menu instead of leaving the screen.
 *
 * State:
 *  - `expanded` is held in `rememberSaveable` so that it survives configuration
 *    changes (rotation, theme switch, process death). Using plain `remember`
 *    would reset to `false` on rotation, which feels broken to users.
 *
 * Note on this API's availability: `FloatingActionButtonMenu` ships in
 * `androidx.compose.material3:material3:1.5.0-alphaXX`. The project pins that
 * version explicitly in `app/build.gradle.kts` to opt in.
 *
 * @param modifier layout modifier. Typically the parent aligns this to BottomEnd.
 * @param onAction invoked when any menu item is tapped. The menu collapses first
 *   so the caller doesn't need to manage that.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FabMenu(
  modifier: Modifier = Modifier,
  onAction: () -> Unit = {}
) {
  // `rememberSaveable` persists this Boolean across rotation / process recreation.
  // For a Boolean it uses the built-in saver — no extra code needed.
  var expanded by rememberSaveable { mutableStateOf(false) }

  // BackHandler only intercepts back when the lambda is non-null, which the
  // `enabled` flag controls. We only want to swallow back when the menu is open.
  BackHandler(expanded) { expanded = false }

  // Each menu item is just (icon, label). Kept local because it's not reused
  // anywhere else. If we wanted to drive this from outside, we'd promote it
  // to a parameter on FabMenu.
  val items: List<Pair<ImageVector, String>> = listOf(
    Icons.Filled.Edit to "Edit",
    Icons.Filled.Favorite to "Favorite",
    Icons.Filled.Share to "Share",
  )

  FloatingActionButtonMenu(
    modifier = modifier,
    expanded = expanded,
    // `button` is the slot for the toggle FAB. The menu uses it as the anchor
    // point and drives the open/close animation around it.
    button = {
      ToggleFloatingActionButton(
        checked = expanded,
        onCheckedChange = { expanded = it }
      ) {
        // Inside this scope, `checkedProgress` is a Float driven by the toggle
        // animation: 0f = collapsed, 1f = expanded. We watch it to swap the
        // icon at the halfway point so the morph feels symmetric.
        //
        // `derivedStateOf` here means we only trigger recomposition when the
        // resulting ImageVector actually changes (crossing 0.5), not every
        // single frame of the animation.
        val imageVector by remember {
          derivedStateOf {
            if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
          }
        }
        Icon(
          painter = rememberVectorPainter(imageVector),
          contentDescription = if (expanded) "Close menu" else "Open menu",
          // `animateIcon` ties the icon's own size/tint to the toggle progress
          // so the swap is smooth instead of a hard cut.
          modifier = Modifier.animateIcon({ checkedProgress })
        )
      }
    }
  ) {
    // The trailing lambda is the `FloatingActionButtonMenuScope` — only
    // `FloatingActionButtonMenuItem` is valid here, which is why the type
    // system stops us from putting arbitrary composables in this slot.
    items.forEach { (icon, label) ->
      FloatingActionButtonMenuItem(
        onClick = {
          expanded = false
          onAction()
        },
        icon = { Icon(icon, contentDescription = null) },
        text = { Text(label) }
      )
    }
  }
}
