package com.example.addictions_edit.presentation.view

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.addictions_edit.presentation.viewmodel.AddictionAddEditViewModel
import java.util.Calendar

@ExperimentalMaterial3Api
@Composable
internal fun DatePickerDialog(viewModel: AddictionAddEditViewModel) {

    val dateState = rememberDatePickerState(
        //не должно быть возврата из viewModel
        // есть смысл вынесни функции по работе с датой и временем в utils !!!!
        initialSelectedDateMillis = viewModel.convertStringDateToLong(viewModel.date),
        //maxDate = currentDate
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

@Composable
internal fun TimePickerDialog(viewModel: AddictionAddEditViewModel) {
    val context = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    val (initialHour, initialMinute) = viewModel.time.split(":").map { it.toInt() }

    val currentMillis = System.currentTimeMillis()

    val maxDateTimeReached  = viewModel.convertStringTimeToLong(viewModel.time) + viewModel.convertStringDateToLong(viewModel.date) >= currentMillis

    android.app.TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            val selectedTimeMillis = viewModel.convertStringTimeToLong("%02d:%02d".format(hour, minute))
            if (!maxDateTimeReached || selectedTimeMillis <= currentMillis) {
                viewModel.onTimeChange(Pair(mHour, mMinute))
                viewModel.showTimeDialogHide()
            } else {
                viewModel.showTimeDialogHide()
                Toast.makeText(context, "Выбранное время еще не наступило", Toast.LENGTH_SHORT).show()
            }

        }, initialHour, initialMinute, true
    ).show()
}
