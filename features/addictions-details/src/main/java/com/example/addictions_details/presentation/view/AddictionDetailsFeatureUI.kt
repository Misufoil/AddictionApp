package com.example.addictions_details.presentation.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.addictions_details.models.AddictionUI
import com.example.addictions_details.presentation.viewmodel.AddictionDetailsViewModel
import com.example.addictions_details.presentation.viewmodel.State
import dev.misufoil.addictions.theme.AddictionTheme
import kotlinx.coroutines.launch
import dev.misufoil.addictions.uikit.R as uikitR


@ExperimentalFoundationApi
@Composable
fun AddictionDetailsScreen(
    modifier: Modifier = Modifier,
    navigateToAddEdit: (String) -> Unit,
    onPopBackStack: () -> Boolean,
) {
    val viewModel: AddictionDetailsViewModel = hiltViewModel()
    val currentState = viewModel.state
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.background(AddictionTheme.colorScheme.surfaceVariant)
    ) { padding ->
        when (currentState) {
            is State.None -> Unit
            is State.Error -> ErrorMessage(currentState, padding)
            is State.Loading -> ProgressIndicator(currentState, padding)
            is State.Success -> AddictionScreen(
                addiction = currentState.addiction,
                showDeleteDialog = { viewModel.showDeleteDialog() },
                navigateToAddEdit = navigateToAddEdit,
                padding = padding,
                totalMoneySaved = viewModel.totalMoneySaved,
                totalCaloriesSaved = viewModel.totalCaloriesSaved,
            )
        }

        if (viewModel.showDeleteDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    coroutineScope.launch {
                        viewModel.deleteAddiction()
                        onPopBackStack()
                    }
                },
                onDismiss = {
                    viewModel.hideDeleteDialog()
                }
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun AddictionScreen(
    addiction: AddictionUI,
    showDeleteDialog: () -> Unit,
    navigateToAddEdit: (String) -> Unit,
    padding: PaddingValues,
    totalMoneySaved: Double,
    totalCaloriesSaved: Double
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val coroutineScope = rememberCoroutineScope()

        InfoSliderComponent(addiction)

        SavingsComponent(totalMoneySaved, totalCaloriesSaved)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ElevatedButton(
                modifier = Modifier.fillMaxWidth().padding(8.dp).weight(1F),
                onClick = {
                    navigateToAddEdit(addiction.id.toString())
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(uikitR.drawable.baseline_edit_square_24),
                    contentDescription = stringResource(id = uikitR.string.edit_addiction)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = stringResource(id = uikitR.string.edit_button))
            }

            ElevatedButton(
                modifier = Modifier.fillMaxWidth().padding(8.dp).weight(1F),
                onClick = {
                    coroutineScope.launch {
                        showDeleteDialog()
                    }
                },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(uikitR.drawable.baseline_delete_outline_24),
                    contentDescription = stringResource(id = uikitR.string.delete_addiction)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = stringResource(id = uikitR.string.delete_button))
            }
        }
    }
}