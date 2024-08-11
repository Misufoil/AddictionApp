package com.example.addictions_edit.presentation.view


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.addictions_edit.presentation.viewmodel.AddictionAddEditViewModel
import dev.misufoil.addictions.theme.AddictionTheme
import kotlinx.coroutines.launch


@ExperimentalMaterial3Api
@Composable
fun AddictionAddEditScreen(
    onPopBackStack: () -> Boolean,
    modifier: Modifier = Modifier
) {
    AddictionAddEditScreen(
        viewModel = hiltViewModel(),
        modifier = modifier,
        onPopBackStack
    )
}

@ExperimentalMaterial3Api
@Composable
private fun AddictionAddEditScreen(
    viewModel: AddictionAddEditViewModel,
    modifier: Modifier,
    onPopBackStack: () -> Boolean
) {
    val currentState = viewModel.state

    Scaffold(
        modifier = Modifier.background(AddictionTheme.colorScheme.surfaceVariant)
    ) { padding ->
        when (currentState) {
            is AddictionAddEditViewModel.State.None -> Unit
            is AddictionAddEditViewModel.State.Error -> ErrorMessage(currentState, padding)
            is AddictionAddEditViewModel.State.Loading -> ProgressIndicator(currentState, padding)
            is AddictionAddEditViewModel.State.Success -> AddictionScreen(
                viewModel,
                onPopBackStack,
                padding
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun AddictionScreen(
    viewModel: AddictionAddEditViewModel,
    onPopBackStack: () -> Boolean,
    padding: PaddingValues
) {
    val addictionUiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.Start,
    ) {
        TypeComponent(
            addictionUiState.type,
            onClick = { viewModel.onBottomSheetShow() }
        )
        DateAndTimeComponent(viewModel, addictionUiState.date, addictionUiState.time)
        FrequencyOfUseComponent(
            viewModel,
            addictionUiState.daysPerWeek,
            addictionUiState.timesInDay
        )

        ElevatedButton(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            onClick = {
                coroutineScope.launch {
                    viewModel.saveAddiction()
                    onPopBackStack()
                }
            },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(dev.misufoil.addictions.uikit.R.drawable.baseline_save_24),
                contentDescription = stringResource(id = dev.misufoil.addictions.uikit.R.string.save_addiction)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(stringResource(id = dev.misufoil.addictions.uikit.R.string.save_button))
        }
    }

    if (viewModel.showBottomSheet) {
        ModalBottomSheetComponent(viewModel, padding)
    }
    viewModel.toastMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.clearToastMessage()
    }
}

