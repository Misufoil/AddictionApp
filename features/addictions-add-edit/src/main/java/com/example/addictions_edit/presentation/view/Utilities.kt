package com.example.addictions_edit.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.addictions_edit.presentation.viewmodel.State

@Composable
internal fun ErrorMessage(state: State.Error, padding: PaddingValues) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error during update")
    }
}

@Composable
internal fun ProgressIndicator(
    state: State.Loading,
    padding: PaddingValues
) {
    Column(modifier = Modifier.padding(padding)) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}