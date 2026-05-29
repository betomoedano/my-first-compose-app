package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.components.BetoPic

/**
 * The Profile tab — currently a placeholder.
 *
 * Same pattern as [FavoritesScreen]: dedicated file per tab so each can grow
 * independently without forcing churn on the others.
 */
@Composable
fun ProfileScreen() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center
  ) {
    BetoPic()
    Text("Profile", style = MaterialTheme.typography.headlineMedium)
  }
}
