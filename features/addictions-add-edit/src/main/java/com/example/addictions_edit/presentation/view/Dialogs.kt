package com.example.addictions_edit.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.core_utils.date_time_utils.convertStringDateToLong

@ExperimentalMaterial3Api
@Composable
internal fun DatePickerDialog(
    showDateDialog: Boolean,
    updateDateDialogState: (Boolean) -> Unit,
    onDateChange: (Long) -> Unit,
    date: String
) {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = convertStringDateToLong(date),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    if (showDateDialog) {
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = {
                updateDateDialogState(false)
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dateState.selectedDateMillis != null) {
                            onDateChange(dateState.selectedDateMillis!!)
                        }
                        updateDateDialogState(false)
                    }
                ) {
                    Text(text = stringResource(id = dev.misufoil.addictions.uikit.R.string.ok))
                }
            },
            dismissButton = {
                Button(
                    onClick = { updateDateDialogState(false) }
                ) {
                    Text(text = stringResource(id = dev.misufoil.addictions.uikit.R.string.cancel))
                }
            },
        ) {
            DatePicker(
                state = dateState,
                showModeToggle = true,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun TimePickerDialog(
    time: String,
    onCancel: () -> Unit,
    onConfirm: (TimePickerState) -> Unit,
    modifier: Modifier = Modifier
) {
    var mode: DisplayMode by remember { mutableStateOf(DisplayMode.Picker) }

    val (initialHour, initialMinute) = time.split(":").map { it.toInt() }
    val timeState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    PickerDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = { Text(stringResource(id = dev.misufoil.addictions.uikit.R.string.select_time)) },
        buttons = {
            DisplayModeToggleButton(
                displayMode = mode,
                onDisplayModeChange = { mode = it },
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onCancel) {
                Text(stringResource(id = dev.misufoil.addictions.uikit.R.string.cancel))
            }
            TextButton(onClick = { onConfirm(timeState) }) {
                Text(stringResource(id = dev.misufoil.addictions.uikit.R.string.ok))
            }
        },
    ) {
        val contentModifier = Modifier.padding(horizontal = 24.dp)
        when (mode) {
            DisplayMode.Picker -> TimePicker(modifier = contentModifier, state = timeState)
            DisplayMode.Input -> TimeInput(modifier = contentModifier, state = timeState)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun DisplayModeToggleButton(
    displayMode: DisplayMode,
    onDisplayModeChange: (DisplayMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (displayMode) {
        DisplayMode.Picker -> IconButton(
            modifier = modifier,
            onClick = { onDisplayModeChange(DisplayMode.Input) },
        ) {
            Icon(
                painter = painterResource(id = dev.misufoil.addictions.uikit.R.drawable.baseline_keyboard_24),
                contentDescription = stringResource(id = dev.misufoil.addictions.uikit.R.string.input_mode),
                //"Switch to input mode"
            )
        }

        DisplayMode.Input -> IconButton(
            modifier = modifier,
            onClick = { onDisplayModeChange(DisplayMode.Picker) },
        ) {
            Icon(
                painter = painterResource(id = dev.misufoil.addictions.uikit.R.drawable.baseline_schedule_24),
                contentDescription = stringResource(id = dev.misufoil.addictions.uikit.R.string.selection_mode),
                //"Switch to picker mode"
            )
        }
    }
}

@Composable
fun PickerDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    buttons: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min),
            shape = AddictionTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Title
                CompositionLocalProvider(LocalContentColor provides AddictionTheme.colorScheme.onSurfaceVariant) {
                    ProvideTextStyle(AddictionTheme.typography.labelLarge) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 24.dp)
                                .padding(top = 16.dp, bottom = 20.dp),
                        ) {
                            title()
                        }
                    }
                }
                // Content
                CompositionLocalProvider(LocalContentColor provides AlertDialogDefaults.textContentColor) {
                    content()
                }
                // Buttons
                CompositionLocalProvider(LocalContentColor provides AddictionTheme.colorScheme.primary) {
                    ProvideTextStyle(AddictionTheme.typography.labelLarge) {
                       Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp, end = 6.dp, start = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        ) {
                            buttons()
                        }
                    }
                }
            }
        }
    }
}

