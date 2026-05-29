package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

/**
 * A modal bottom sheet that previews an image edge-to-edge.
 *
 * The Material 3 `ModalBottomSheet` has three positions:
 *  - **Hidden** — off-screen.
 *  - **PartiallyExpanded** — about half the screen height. This is the default
 *    starting position and is the "50% detent" we want.
 *  - **Expanded** — full screen, reached when the user drags up.
 *
 * Making the image cover the entire sheet area takes three deliberate moves,
 * because `ModalBottomSheet` reserves space by default for chrome we do not
 * want here:
 *
 *  1. **`dragHandle = null`** — by default the sheet shows a centered drag
 *     handle above the content. Setting this to `null` removes the slot, so
 *     the image starts at the very top of the sheet. The user can still
 *     drag the sheet from anywhere on its surface; the handle was only a
 *     visual affordance.
 *
 *  2. **`contentWindowInsets = { WindowInsets(0) }`** — the default value is
 *     `BottomSheetDefaults.windowInsets`, which adds bottom padding equal to
 *     the system navigation bar inset (so content isn't covered by the gesture
 *     pill / nav buttons). For an immersive image preview we want the image
 *     to extend behind the nav bar, so we pass an empty `WindowInsets(0)`.
 *
 *  3. **`Modifier.fillMaxSize()`** — without 1 and 2 above, this would still
 *     leave gaps at the top and bottom. With them, `fillMaxSize` consumes the
 *     entire sheet area at whatever detent the sheet is at.
 *
 *  `ContentScale.Crop` fills the box without letterboxing; for a preview that
 *  is what users expect. Switch to `Fit` if you ever need to guarantee the
 *  whole image is visible (with black bars on the sides).
 *
 * @param imageUrl URL of the image to preview.
 * @param contentDescription accessibility description for the image.
 * @param onDismiss called when the sheet is dismissed by drag-down, back,
 *   or scrim tap.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePreviewSheet(
  imageUrl: String,
  contentDescription: String,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState()

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    dragHandle = null,
    contentWindowInsets = { WindowInsets(0) }
  ) {
    AsyncImage(
      model = imageUrl,
      contentDescription = contentDescription,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )
  }
}
