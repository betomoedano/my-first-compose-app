package com.example.myapplication.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

/**
 * The set of modes the split button can fire.
 *
 * An enum is used so:
 *  - The icon and label live next to the value they describe (no parallel maps).
 *  - The compiler enforces that the dropdown lists every available mode.
 *  - Callers receive a typed value in their `onAction` callback, not a String,
 *    so a typo in a `when` branch becomes a build error instead of a runtime
 *    bug.
 */
enum class SplitButtonMode(val label: String, val icon: ImageVector) {
  EDIT("Edit", Icons.Filled.Edit),
  SHARE("Share", Icons.Filled.Share),
  DELETE("Delete", Icons.Filled.Delete)
}

/**
 * A Material 3 **Split Button** whose chevron drives a real dropdown menu.
 *
 * Anatomy:
 *  - **Leading button**: shows the icon + label of the currently selected
 *    [SplitButtonMode] and fires [onAction] with that mode when tapped.
 *  - **Trailing button** (chevron): toggles a [DropdownMenu] of every available
 *    mode. Picking one updates the leading button and closes the menu.
 *
 * This is the classic "primary action + variants" pattern you see in apps
 * like Gmail (compose / reply / reply-all) and IDEs (run / debug / profile):
 * the most common action is one tap, and rare variants are one extra tap.
 *
 * Why a `Box` wraps the trailing button:
 *  - `DropdownMenu` opens as a `Popup`, anchored to the nearest layout parent.
 *  - Wrapping the chevron + menu in a `Box` makes the chevron the anchor —
 *    the menu drops down right below it instead of from the screen corner.
 *
 * State held inside (and why):
 *  - `selectedMode`: the active mode. `rememberSaveable` so rotation doesn't
 *    flip it back to EDIT mid-interaction. Enums are saveable via the default
 *    AutoSaver because Kotlin enums implement `Serializable`.
 *  - `menuOpen`: whether the dropdown is showing. `rememberSaveable` so if the
 *    user opens the menu and rotates, the menu is still open after rotation.
 *
 * The opt-ins:
 *  - `ExperimentalMaterial3ExpressiveApi` for `SplitButtonLayout` family.
 *  - `ExperimentalMaterial3Api` for `TooltipBox` and friends.
 *
 * @param modifier layout modifier.
 * @param onAction called with the currently-selected [SplitButtonMode] when
 *   the user taps the leading button. The composable does **not** call this
 *   when the user only picks from the dropdown — selecting only updates the
 *   internal state. This matches the user's mental model: "pick from menu"
 *   ≠ "perform action".
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditSplitButton(
  modifier: Modifier = Modifier,
  onAction: (SplitButtonMode) -> Unit = {}
) {
  var selectedMode by rememberSaveable { mutableStateOf(SplitButtonMode.EDIT) }
  var menuOpen by rememberSaveable { mutableStateOf(false) }

  SplitButtonLayout(
    modifier = modifier,
    leadingButton = {
      SplitButtonDefaults.LeadingButton(onClick = { onAction(selectedMode) }) {
        Icon(
          selectedMode.icon,
          modifier = Modifier.size(SplitButtonDefaults.LeadingIconSize),
          contentDescription = null // the Text next to it is the label
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(selectedMode.label)
      }
    },
    trailingButton = {
      val description = "Choose action"
      // The Box exists so the DropdownMenu's Popup anchors to the chevron's
      // position, not to some random parent. Without it the menu still opens
      // but its placement is harder to reason about.
      Box {
        TooltipBox(
          positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
          ),
          tooltip = {
            PlainTooltip(
              modifier = Modifier.semantics {
                // Workaround so screen readers announce the tooltip text.
                liveRegion = LiveRegionMode.Assertive
                paneTitle = description
              }
            ) {
              Text(description)
            }
          },
          state = rememberTooltipState()
        ) {
          SplitButtonDefaults.TrailingButton(
            checked = menuOpen,
            onCheckedChange = { menuOpen = it },
            modifier = Modifier.semantics {
              stateDescription = if (menuOpen) "Expanded" else "Collapsed"
              contentDescription = description
            }
          ) {
            // Smoothly tween the chevron between 0° and 180° when the menu
            // opens/closes — instead of snapping. `graphicsLayer` is used
            // (rather than a rotate modifier) because rotation only needs to
            // invalidate draw, not layout, which is cheaper per frame.
            val rotation: Float by animateFloatAsState(
              targetValue = if (menuOpen) 180f else 0f,
              label = "Trailing Icon Rotation"
            )
            Icon(
              Icons.Filled.KeyboardArrowDown,
              modifier = Modifier
                .size(SplitButtonDefaults.TrailingIconSize)
                .graphicsLayer { rotationZ = rotation },
              contentDescription = null
            )
          }
        }

        DropdownMenu(
          expanded = menuOpen,
          // The menu calls this when the user taps outside, presses back, or
          // taps a menu item. We mirror it onto our `menuOpen` state.
          onDismissRequest = { menuOpen = false }
        ) {
          // Iterating over `entries` means adding a new mode auto-appears in
          // the menu — no parallel list to keep in sync.
          SplitButtonMode.entries.forEach { mode ->
            DropdownMenuItem(
              text = { Text(mode.label) },
              leadingIcon = { Icon(mode.icon, contentDescription = null) },
              onClick = {
                selectedMode = mode
                menuOpen = false
              }
            )
          }
        }
      }
    }
  )
}
