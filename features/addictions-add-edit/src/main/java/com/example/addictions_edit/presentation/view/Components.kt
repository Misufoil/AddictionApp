package com.example.addictions_edit.presentation.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addictions_edit.presentation.viewmodel.AddictionAddEditViewModel
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.addictions.uikit.R
import dev.misufoil.core_utils.date_time_utils.convertStringDateTimeToLong
import dev.misufoil.core_utils.models.AddictionTypes
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
internal fun TypeComponent(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = stringResource(id = R.string.select_addiction),
        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
        style = AddictionTheme.typography.titleLarge
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.large)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = AddictionTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row {
            Text(
                modifier = Modifier.padding(16.dp).weight(1f),
                text = text,
                style = AddictionTheme.typography.bodyLarge
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_down_24),
                contentDescription = null,
                modifier = Modifier.padding(16.dp).size(24.dp).clearAndSetSemantics {}
            )
        }
    }
}


@ExperimentalMaterial3Api
@Composable
internal fun DateAndTimeComponent(viewModel: AddictionAddEditViewModel) {


    Text(
        text = stringResource(id = R.string.date_and_time),
        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
        style = AddictionTheme.typography.titleLarge
    )


    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.large)
            .clickable {
                viewModel.showDateDialogShow()
            },
        colors = CardDefaults.cardColors(
            containerColor = AddictionTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                text = viewModel.date,
                //text = viewModel.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)),
                style = AddictionTheme.typography.bodyLarge
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_down_24),
                contentDescription = null,
                modifier = Modifier.padding(16.dp).size(24.dp).clearAndSetSemantics {}
            )
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.large)
            .clickable {
                viewModel.showTimeDialogShow()
            },
        colors = CardDefaults.cardColors(
            containerColor = AddictionTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row {
            Text(
                modifier = Modifier.padding(16.dp).weight(1f),
                text = viewModel.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = AddictionTheme.typography.bodyLarge
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_down_24),
                contentDescription = null,
                modifier = Modifier.padding(16.dp).size(24.dp).clearAndSetSemantics {}
            )
        }
    }

    if (viewModel.showDateDialog) {
        DatePickerDialog(viewModel)
    }

    if (viewModel.showTimeDialog) {
        val context = LocalContext.current

        TimePickerDialog(
            onCancel = {
                viewModel.showTimeDialogHide()
            },
            onConfirm = { timePickerState ->
                val hour = timePickerState.hour
                val minute = timePickerState.minute
                val currentMillis = System.currentTimeMillis()

                val maxDateTimeReached =
                    convertStringDateTimeToLong(viewModel.date, "%02d:%02d".format(hour, minute))

                if (maxDateTimeReached <= currentMillis) {
                    viewModel.onTimeChange(Pair(hour, minute))
                } else {
                    Toast.makeText(context, context.getString(R.string.time_not_come), Toast.LENGTH_SHORT)
                        .show()
                }
                viewModel.showTimeDialogHide()
            },
            time = viewModel.time
        )
    }
}


@ExperimentalMaterial3Api
@Composable
internal fun FrequencyOfUseComponent(viewModel: AddictionAddEditViewModel) {
    Text(
        text = stringResource(id = R.string.frequency_of_use),
        modifier = Modifier.padding(start = 3.dp, top = 4.dp),
        style = AddictionTheme.typography.titleLarge
    )
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.large),
        colors = CardDefaults.cardColors(
            containerColor = AddictionTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = stringResource(id = R.string.days_per_week),
                modifier = Modifier.padding(16.dp).weight(1f),
                style = AddictionTheme.typography.bodyLarge
            )

            IconButton(
                onClick = { if (viewModel.daysPerWeek > 1) viewModel.onDaysPerWeekChange(viewModel.daysPerWeek - 1) },
                enabled = viewModel.daysPerWeek > 1
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_remove_24),
                    contentDescription = stringResource(id = R.string.reduce_days),
                    tint = if (viewModel.daysPerWeek > 1) IconButtonDefaults.iconButtonColors().contentColor else IconButtonDefaults.iconButtonColors().disabledContentColor
                )
            }

            Text(
                text = viewModel.daysPerWeek.toString(),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = AddictionTheme.typography.bodyLarge
            )

            IconButton(
                onClick = { if (viewModel.daysPerWeek < 7) viewModel.onDaysPerWeekChange(viewModel.daysPerWeek + 1) },
                enabled = viewModel.daysPerWeek < 7
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_add_24),
                    contentDescription = stringResource(id = R.string.increase_days),
                    tint = if (viewModel.daysPerWeek < 7) IconButtonDefaults.iconButtonColors().contentColor else IconButtonDefaults.iconButtonColors().disabledContentColor
                )
            }
        }
    }


    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.large,),
        colors = CardDefaults.cardColors(
            containerColor = AddictionTheme.colorScheme.surfaceVariant,
        ),
        //modifier = modifier
        //            .padding(8.dp)
        //            .fillMaxWidth()
        //            .clickable {
        //                navigateToDetails(addiction.type.description)
        //            }
        //            .shadow(elevation = 2.dp, shape = MaterialTheme.shapes.large),
        //        colors = CardDefaults.cardColors(
        //            containerColor = AddictionTheme.colorScheme.surfaceVariant,
        //        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = stringResource(id = R.string.once_a_day),
                modifier = Modifier.padding(16.dp).weight(1f),
                style = AddictionTheme.typography.bodyLarge
            )

            IconButton(
                onClick = { if (viewModel.timesInDay > 1) viewModel.onTimesInDayChange(viewModel.timesInDay - 1) },
                enabled = viewModel.timesInDay > 1
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_remove_24),
                    contentDescription = stringResource(id = R.string.increase_times),
                    tint = if (viewModel.timesInDay > 1) IconButtonDefaults.iconButtonColors().contentColor else IconButtonDefaults.iconButtonColors().disabledContentColor
                )
            }

            Text(
                text = viewModel.timesInDay.toString(),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = AddictionTheme.typography.bodyLarge
            )

            IconButton(
                onClick = { if (viewModel.timesInDay < 100) viewModel.onTimesInDayChange(viewModel.timesInDay + 1) },
                enabled = viewModel.timesInDay < 100
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_add_24),
                    contentDescription = stringResource(id = R.string.reduce_times),
                    tint = if (viewModel.timesInDay < 100) IconButtonDefaults.iconButtonColors().contentColor else IconButtonDefaults.iconButtonColors().disabledContentColor
                )
            }
        }
    }

}

@ExperimentalMaterial3Api
@Composable
internal fun ModalBottomSheetComponent(
    viewModel: AddictionAddEditViewModel,
    padding: PaddingValues
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val addictionList = AddictionTypes.entries
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = {
            viewModel.onBottomSheetHide()
        },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(addictionList) { addiction ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .shadow(elevation = 1.dp, shape = MaterialTheme.shapes.medium)
                            .clickable {
                                coroutineScope.launch {
                                    viewModel.onTypeChange(addiction)
                                    viewModel.onBottomSheetHide()
                                    modalBottomSheetState.hide()
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = AddictionTheme.colorScheme.surfaceVariant,
                        ),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = addiction.description,
                                modifier = Modifier.padding(
                                    top = 4.dp,
                                    bottom = 4.dp,
                                    start = 10.dp,
                                    end = 10.dp
                                ),
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        }
    )
}