package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.components.BetoPic
import com.example.myapplication.ui.components.CarouselExample
import com.example.myapplication.ui.components.EditSplitButton
import com.example.myapplication.ui.components.ImagePreviewSheet

/**
 * The Home tab.
 *
 * Layout, top to bottom:
 *  1. A [Row] aligned to the end, hosting the [EditSplitButton] in the top-right corner.
 *  2. The circular [BetoPic] profile image.
 *  3. The horizontal [CarouselExample]. Tapping any image shows it in
 *     [ImagePreviewSheet], a modal bottom sheet at the half-screen detent.
 *
 * State that lives here:
 *  - **`previewedImage`** — the (url, description) pair of the carousel image
 *    currently being previewed, or `null` when the sheet is closed. Held at
 *    this level (and not inside the carousel) because:
 *      - The sheet must outlive the carousel item's draw cycle.
 *      - Lifting state up is the standard Compose pattern when a sibling
 *        component needs to react to another's events.
 *
 * The composables below this screen are intentionally **stateless**: they
 * receive callbacks and render. That makes them previewable and testable
 * without any setup.
 */
@Composable
fun HomeScreen() {
  // `Pair<String, String>?` — first is the URL, second is the content description.
  // Using `rememberSaveable` so the sheet survives rotation if it was open.
  var previewedImage: Pair<String, String>? by rememberSaveable {
    mutableStateOf(null)
  }

  Column {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.End
    ) {
      EditSplitButton()
    }
    CarouselExample(
      onImageClick = { url, description ->
        previewedImage = url to description
      }
    )
  }

  // Only compose the sheet when there's an image to show. Compose handles the
  // enter/exit animations as part of the sheet's own lifecycle. When the user
  // dismisses, we clear state so this branch stops composing.
  previewedImage?.let { (url, description) ->
    ImagePreviewSheet(
      imageUrl = url,
      contentDescription = description,
      onDismiss = { previewedImage = null }
    )
  }
}
