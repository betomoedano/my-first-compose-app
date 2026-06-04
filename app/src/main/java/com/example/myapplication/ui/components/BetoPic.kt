package com.example.myapplication.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

/**
 * A circular profile picture loaded from the network.
 *
 * Uses Coil's [AsyncImage] which handles:
 *  - Fetching the bytes off the main thread.
 *  - Decoding the image.
 *  - Showing a placeholder while loading (none here — we let it stay empty).
 *  - Caching, so the second render is instant.
 *
 * `Modifier` order matters in Compose: each modifier wraps the next. We apply
 * size first, then `clip` to make the bounds circular, then `border` so the
 * border is drawn on top of the clipped circle. Reversing the order would
 * clip the border away.
 *
 * @param modifier optional modifier — callers can add padding, alignment, etc.
 *   Always expose a `modifier` param on reusable composables so the parent
 *   can control layout without the component needing to know about it.
 */
@Composable
fun BetoPic(modifier: Modifier = Modifier) {
  AsyncImage(
    model = "https://github.com/betomoedano.png",
    contentDescription = "Profile picture",
    contentScale = ContentScale.Crop,
    modifier = modifier
      .size(200.dp)
      .clip(CircleShape)
      .border(2.dp, Color.White, CircleShape)
  )
}
