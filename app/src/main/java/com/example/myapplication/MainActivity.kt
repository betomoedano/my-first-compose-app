package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * The single Activity that hosts the entire Compose app.
 *
 * This class is intentionally tiny. Its only jobs are:
 *  1. Wire Android lifecycle (`onCreate`) into Compose (`setContent`).
 *  2. Apply the app theme.
 *  3. Hand a callback down to the Compose tree.
 *
 * All UI lives in [MyApp]. Keeping the Activity thin means:
 *  - The UI is testable without spinning up Android.
 *  - Replacing or adding another Activity later (deep links, share targets)
 *    doesn't require detangling UI from lifecycle code.
 *
 * `enableEdgeToEdge()` lets the app draw behind the system bars (status &
 * navigation). Compose handles the insets via `Scaffold` so content isn't
 * obscured.
 */
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MyApp(onClick = { logClick() })
      }
    }
  }

  /** Logged whenever any FAB menu item is tapped. Kept here so non-Compose
   *  Android code (analytics, etc.) has a single hook to grow from. */
  private fun logClick() {
    Log.d("[Beto Logs]", "Beto was here!")
  }
}
