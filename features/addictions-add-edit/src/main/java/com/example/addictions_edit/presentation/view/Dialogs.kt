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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.addictions_edit.presentation.viewmodel.AddictionAddEditViewModel
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.core_utils.date_time_utils.convertStringDateToLong

@ExperimentalMaterial3Api
@Composable
internal fun DatePickerDialog(viewModel: AddictionAddEditViewModel) {

    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = convertStringDateToLong(viewModel.date),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    if (viewModel.showDateDialog) {
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = {
                viewModel.showDateDialogHide()
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dateState.selectedDateMillis != null) {
                            viewModel.onDateChange(dateState.selectedDateMillis!!)
                        }
                        viewModel.showDateDialogHide()
                    }
                ) {
                    Text(text = "ОК")
                }
            },
            dismissButton = {
                Button(
                    onClick = { viewModel.showDateDialogHide() }
                ) {
                    Text(text = "Отмена")
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
        title = { Text("Select hour") },
        buttons = {
            DisplayModeToggleButton(
                displayMode = mode,
                onDisplayModeChange = { mode = it },
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
            TextButton(onClick = { onConfirm(timeState) }) {
                Text("Confirm")
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
                contentDescription = "Переключить в режим ввода",
                //"Switch to input mode"
            )
        }

        DisplayMode.Input -> IconButton(
            modifier = modifier,
            onClick = { onDisplayModeChange(DisplayMode.Picker) },
        ) {
            Icon(
                painter = painterResource(id = dev.misufoil.addictions.uikit.R.drawable.baseline_schedule_24),
                contentDescription = "Переключить в режим выбора",
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
                        // TODO This should wrap on small screens, but we can't use AlertDialogFlowRow as it is no public
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

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TimePickerDialog(
//    time: String,
//    title: String = "Select Time",
//    onCancel: () -> Unit,
//    onConfirm: (TimePickerState) -> Unit,
//    toggle: @Composable () -> Unit = {},
//    content: @Composable () -> Unit,
//) {
//    val (initialHour, initialMinute) = time.split(":").map { it.toInt() }
//    val timePickerState = rememberTimePickerState(
//        initialHour = initialHour,
//        initialMinute = initialMinute,
//        is24Hour = true,
//    )
//    Dialog(
//        onDismissRequest = onCancel,
//        properties = DialogProperties(
//            usePlatformDefaultWidth = false
//        ),
//    ) {
//        Surface(
//            shape = AddictionTheme.shapes.extraLarge,
//            tonalElevation = 6.dp,
//            modifier = Modifier
//                .width(IntrinsicSize.Min)
//                .height(IntrinsicSize.Min)
//                .background(
//                    shape = MaterialTheme.shapes.extraLarge,
//                    color = MaterialTheme.colorScheme.surface
//                ),
//        ) {
//            Column(
//                modifier = Modifier.padding(24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 20.dp),
//                    text = title,
//                    style = MaterialTheme.typography.labelMedium
//                )
//                TimePicker(timePickerState)
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 8.dp, end = 6.dp, start = 6.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
//                ) {
//                    DisplayModeToggleButton(
//                        displayMode = mode,
//                        onDisplayModeChange = { mode = it },
//                    )
//                    //toggle()
//                    Spacer(modifier = Modifier.weight(1f))
//                    TextButton(
//                        onClick = onCancel
//                    ) { Text("Cancel") }
//                    TextButton(
//                        onClick = {
//                            onConfirm(timePickerState)
//                        }
//                    ) {
//                        Text("OK")
//                    }
//                }
//            }
//        }
//    }
//}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun DisplayModeToggleButton(
//    displayMode: DisplayMode,
//    onDisplayModeChange: (DisplayMode) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    when (displayMode) {
//        DisplayMode.Picker -> IconButton(
//            modifier = modifier,
//            onClick = { onDisplayModeChange(DisplayMode.Input) },
//        ) {
//            Icon(
//                painter = painterResource(id = dev.misufoil.addictions.uikit.R.drawable.baseline_keyboard_24),
//                contentDescription = "",
//            )
//        }
//
//        DisplayMode.Input -> IconButton(
//            modifier = modifier,
//            onClick = { onDisplayModeChange(DisplayMode.Picker) },
//        ) {
//            Icon(
//                painter = painterResource(id = dev.misufoil.addictions.uikit.R.drawable.baseline_schedule_24),
//                contentDescription = "",
//            )
//        }
//    }
//}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//internal fun TimePickerDialog(viewModel: AddictionAddEditViewModel) {
//    val context = LocalContext.current
//    val (initialHour, initialMinute) = viewModel.time.split(":").map { it.toInt() }
//
//    val currentMillis = System.currentTimeMillis()
//
//    val maxDateTimeReached =
//        convertStringTimeToLong(viewModel.time) + convertStringDateToLong(
//            viewModel.date
//        ) >= currentMillis
//
//    android.app.TimePickerDialog(
//        context,
//        { _, hour: Int, minute: Int ->
//            val selectedTimeMillis =
//                convertStringTimeToLong("%02d:%02d".format(hour, minute))
//            if (!maxDateTimeReached || selectedTimeMillis <= currentMillis) {
//                viewModel.onTimeChange(Pair(hour, minute))
//            } else {
//                Toast.makeText(context, "Выбранное время еще не наступило", Toast.LENGTH_SHORT)
//                    .show()
//            }
//            viewModel.showTimeDialogHide()
//        }, initialHour, initialMinute, true
//    ).show()
//}


//@Composable
//fun MTimePickerDialog(
//    time: String,
//    onConfirm: (TimePickerState) -> Unit,
//    onDismiss: () -> Unit,
//) {
//
//    val currentTime = Calendar.getInstance()
//
//    val timePickerState = rememberTimePickerState(
//        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
//        initialMinute = currentTime.get(Calendar.MINUTE),
//        is24Hour = true,
//    )
//
//
//    var showDialog by remember { mutableStateOf(true) }
//
//    if (showDialog) {
//        TimePickerDialog(
//            onDismissRequest = {
//                showDialog = false
//                onDismiss()
//            },
//            confirmButton = {
//                TextButton(onClick = {
//                    showDialog = false
//                    onConfirm(timePickerState)
//                }) {
//                    Text("OK")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = {
//                    showDialog = false
//                    onDismiss()
//                }) {
//                    Text("Cancel")
//                }
//            }
//        ) {
//            TimePicker(
//                state = timePickerState,
//            )
//        }
//    }
//}