package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.components.BetoPic

/**
 * The Profile tab — currently a placeholder.
 *
 * Same pattern as [FavoritesScreen]: dedicated file per tab so each can grow
 * independently without forcing churn on the others.
 */
@Preview
@Composable
fun ProfileScreen() {
  LazyColumn() {
  }
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center
  ) {
    BetoPic()
    Text("Profile", style = MaterialTheme.typography.headlineMedium)
    Text("Profile", style = MaterialTheme.typography.headlineMedium)
  }
}
