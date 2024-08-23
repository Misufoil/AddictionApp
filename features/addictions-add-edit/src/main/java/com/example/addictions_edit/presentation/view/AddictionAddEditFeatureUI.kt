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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.addictions_edit.models.AddictionUI
import com.example.addictions_edit.presentation.viewmodel.AddictionAddEditViewModel
import com.example.addictions_edit.presentation.viewmodel.State
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.core_utils.date_time_utils.convertStringDateTimeToLong
import dev.misufoil.addictions.uikit.R as uikitR

@ExperimentalMaterial3Api
@Composable
fun AddictionAddEditScreen(
    onPopBackStack: () -> Boolean,
    modifier: Modifier = Modifier
) {
    val viewModel: AddictionAddEditViewModel = hiltViewModel()
    val currentState = viewModel.state

    Scaffold(
        modifier = Modifier.background(AddictionTheme.colorScheme.surfaceVariant)
    ) { padding ->
        when (currentState) {
            is State.None -> Unit
            is State.Error -> ErrorMessage(currentState, padding)
            is State.Loading -> ProgressIndicator(currentState, padding)
            is State.Success -> AddictionScreen(
                padding = padding,
                addictionUiState = viewModel.uiState.collectAsState().value,
                toastMessageState = viewModel.toastMessageState.collectAsState().value,
                timeDialogState = viewModel.timeDialogState.collectAsState().value,
                dateDialogState = viewModel.dateDialogState.collectAsState().value,
                showBottomSheet = viewModel.showBottomSheet.collectAsState().value,
                updateBottomSheetState = { show: Boolean -> viewModel.updateBottomSheetState(show) },
                updateDateDialogState = { show: Boolean -> viewModel.updateDateDialogState(show) },
                updateTimeDialogState = { show: Boolean -> viewModel.updateTimeDialogState(show) },
                onDaysPerWeekChange = { daysPerWeek: Int ->
                    viewModel.onDaysPerWeekChange(
                        daysPerWeek
                    )
                },
                onTimesInDayChange = { timesInDay: Int -> viewModel.onTimesInDayChange(timesInDay) },
                onTypeChange = { type: String -> viewModel.onTypeChange(type) },
                onDateChange = { date: Long -> viewModel.onDateChange(date) },
                onTimeChange = { time: Pair<Int, Int> -> viewModel.onTimeChange(time) },
                onToastMessageStateChange = { toast: String? ->
                    viewModel.onToastMessageStateChange(
                        toast
                    )
                },
                onSaveButtonClick = {
                    viewModel.onSaveButtonClick()
                    onPopBackStack()
                }
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun AddictionScreen(
    padding: PaddingValues,
    addictionUiState: AddictionUI,
    toastMessageState: String?,
    timeDialogState: Boolean,
    dateDialogState: Boolean,
    showBottomSheet: Boolean,
    updateBottomSheetState: (Boolean) -> Unit,
    updateDateDialogState: (Boolean) -> Unit,
    updateTimeDialogState: (Boolean) -> Unit,
    onDaysPerWeekChange: (Int) -> Unit,
    onTimesInDayChange: (Int) -> Unit,
    onTypeChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Pair<Int, Int>) -> Unit,
    onToastMessageStateChange: (String?) -> Unit,
    onSaveButtonClick: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            TypeComponent(
                addictionUiState.type,
                updateBottomSheetState,
            )
        }
        item {
            DateAndTimeComponent(
                updateDateDialogState,
                updateTimeDialogState,
                addictionUiState.date,
                addictionUiState.time,
            )
        }
        item {
            FrequencyOfUseComponent(
                onDaysPerWeekChange,
                onTimesInDayChange,
                addictionUiState.daysPerWeek,
                addictionUiState.timesInDay,
            )
        }
        item {
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    onSaveButtonClick()
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(uikitR.drawable.baseline_save_24),
                    contentDescription = stringResource(id = uikitR.string.save_addiction)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(stringResource(id = dev.misufoil.addictions.uikit.R.string.save_button))
            }
        }
    }

    if (timeDialogState) {
        TimePickerDialog(
            onCancel = {
                updateTimeDialogState(false)
            },
            onConfirm = { timePickerState ->
                val hour = timePickerState.hour
                val minute = timePickerState.minute
                val currentMillis = System.currentTimeMillis()

                val maxDateTimeReached =
                    convertStringDateTimeToLong(
                        addictionUiState.date,
                        "%02d:%02d".format(hour, minute)
                    )

                if (maxDateTimeReached <= currentMillis) {
                    onTimeChange(Pair(hour, minute))
                } else {
                    Toast.makeText(
                        context,
                        context.getString(uikitR.string.time_not_come),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                updateTimeDialogState(false)
            },
            time = addictionUiState.time
        )
    }

    if (dateDialogState) {
        DatePickerDialog(dateDialogState, updateDateDialogState, onDateChange, addictionUiState.date)
    }

    if (showBottomSheet) {
        ModalBottomSheetComponent(onTypeChange, updateBottomSheetState, padding)
    }

    toastMessageState?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        onToastMessageStateChange(null)
    }
}

//    val addictionUiState by viewModel.uiState.collectAsState()
//    val toastMessageState by viewModel.toastMessageState.collectAsState()
//    val timeDialogState by viewModel.timeDialogState.collectAsState()
//    val dateDialogState by viewModel.dateDialogState.collectAsState()
//    val showBottomSheet by viewModel.showBottomSheet.collectAsState()
//
//
//    val onBottomSheetShow = remember(viewModel) { { viewModel.onBottomSheetShow() } }
//    val onShowDateDialog = remember(viewModel) { { viewModel.dateDialogShow() } }
//    val onHideDateDialog = remember(viewModel) { { viewModel.dateDialogHide() } }
//    val onShowTimeDialog = remember(viewModel) { { viewModel.timeDialogShow() } }
//    val onHideTimeDialog = remember(viewModel) { { viewModel.timeDialogHide() } }
//    val onBottomSheetHide = remember(viewModel) { { viewModel.onBottomSheetHide() } }
//
//
//    val onDaysPerWeekChange = remember(
//        viewModel,
//        addictionUiState.daysPerWeek
//    ) { { daysPerWeek: Int -> viewModel.onDaysPerWeekChange(daysPerWeek) } }
//
//    val onTimesInDayChange = remember(
//        viewModel,
//        addictionUiState.timesInDay
//    ) { { timesInDay: Int -> viewModel.onTimesInDayChange(timesInDay) } }
//
//    val onTypeChange = remember(viewModel, addictionUiState.timesInDay) {
//        { type: String ->
//            viewModel.onTypeChange(type)
//        }
//    }
//
//    val onDateChange = remember(viewModel, addictionUiState.date) {
//        { date: Long ->
//            viewModel.onDateChange(date)
//        }
//    }