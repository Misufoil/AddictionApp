package com.example.addictions_details.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.misufoil.addictions.theme.AddictionTheme

@Composable
fun AddictionDetailsScreen(
    modifier: Modifier = Modifier,
    navigateToAddEdit: (String) -> Unit,
    onPopBackStack: () -> Boolean,
) {
    AddictionAddEditScreen(
        viewModel = hiltViewModel(),
        navigateToAddEdit = navigateToAddEdit,
        onPopBackStack = onPopBackStack,
        modifier = modifier
    )
}

@Composable
private fun AddictionAddEditScreen(
    viewModel: AddictionDetailsViewModel,
    navigateToAddEdit: (String) -> Unit,
    onPopBackStack: () -> Boolean,
    modifier: Modifier
) {
    val currentState = viewModel.state

    Scaffold(
        modifier = Modifier.background(AddictionTheme.colorScheme.surfaceVariant)
    ) { padding ->
        when (currentState) {
            is AddictionDetailsViewModel.State.None -> Unit
            is AddictionDetailsViewModel.State.Error -> ErrorMessage(currentState, padding)
            is AddictionDetailsViewModel.State.Loading -> ProgressIndicator(currentState, padding)
            is AddictionDetailsViewModel.State.Success -> AddictionScreen(
                viewModel,
                navigateToAddEdit,
                onPopBackStack,
                padding
            )
        }
    }
}

@Composable
private fun AddictionScreen(
    viewModel: AddictionDetailsViewModel,
    navigateToAddEdit: (String) -> Unit,
    onPopBackStack: () -> Boolean,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.Start,
    ) {
        Button(
            onClick = {
                navigateToAddEdit(viewModel.state.addiction?.type.toString())
            }
        ) {
            Text("Редактировать")
            Text(viewModel.state.addiction?.type.toString())
        }
    }
}


@Composable
private fun ErrorMessage(state: AddictionDetailsViewModel.State.Error, padding: PaddingValues) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error during update ${state.addiction?.type}")
    }
}

@Composable
private fun ProgressIndicator(
    state: AddictionDetailsViewModel.State.Loading,
    padding: PaddingValues
) {
    Column(modifier = Modifier.padding(padding)) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}


