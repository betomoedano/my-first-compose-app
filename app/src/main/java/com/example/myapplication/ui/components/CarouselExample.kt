package com.example.myapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

/**
 * A horizontal Material 3 carousel showing a series of clickable images.
 *
 * `HorizontalMultiBrowseCarousel` is the "multi-browse" variant of the M3 carousel:
 *  - Multiple items are visible at once.
 *  - As the user swipes, items at the edges shrink/grow with the spec's
 *    mask animation — that's what `maskClip` is doing on each `AsyncImage`.
 *
 * `rememberCarouselState { items.count() }` keeps scroll state across recompositions.
 * The lambda returns the total item count; it is re-read whenever the list size
 * changes, so the state stays in sync if items are added or removed.
 *
 * The data list is held inside the composable on purpose — for a learning
 * example we don't need it injected. If this carousel ever needed to show
 * dynamic data, the right move is to lift `items` into a parameter, not to
 * reach into a singleton from inside.
 *
 * `@OptIn(ExperimentalMaterial3Api::class)` — the carousel API is still marked
 * experimental in Material 3. Opting in means we accept the API may change.
 *
 * @param onImageClick called when the user taps any image. We pass back the
 *   image URL and its content description so the caller can show a preview
 *   without needing to know about our internal `CarouselItem` type.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CarouselExample(
  onImageClick: (imageUrl: String, contentDescription: String) -> Unit = { _, _ -> }
) {
  // A local `data class` keeps each row self-describing (id + url + a11y label).
  // Defined inside the composable because no other code needs to know about it.
  data class CarouselItem(
    val id: Int,
    val imageUrl: String,
    val contentDescription: String
  )

  // `remember` caches this list across recompositions so we don't allocate a
  // new List every time Compose re-renders.
  val items = remember {
    listOf(
      CarouselItem(0, "https://picsum.photos/id/1015/600/400", "mountain"),
      CarouselItem(5, "https://github.com/betomoedano.png", "Beto"),
      CarouselItem(1, "https://picsum.photos/id/1016/600/400", "river"),
      CarouselItem(2, "https://picsum.photos/id/1018/600/400", "forest"),
      CarouselItem(3, "https://picsum.photos/id/1019/600/400", "lake"),
      CarouselItem(4, "https://picsum.photos/id/1024/600/400", "bear"),
    )
  }

  HorizontalMultiBrowseCarousel(
    state = rememberCarouselState { items.count() },
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(top = 16.dp, bottom = 16.dp),
    preferredItemWidth = 186.dp,
    itemSpacing = 8.dp,
    contentPadding = PaddingValues(horizontal = 16.dp)
  ) { i ->
    val item = items[i]
    AsyncImage(
      modifier = Modifier
        .height(205.dp)
        // `maskClip` clips this child with the carousel's animated mask.
        // It must be applied *before* `clickable` so the ripple is clipped
        // to the rounded shape too — otherwise you'd see a square ripple.
        .maskClip(MaterialTheme.shapes.extraLarge)
        .clickable { onImageClick(item.imageUrl, item.contentDescription) },
      model = item.imageUrl,
      contentDescription = item.contentDescription,
      contentScale = ContentScale.Crop
    )
  }
}
