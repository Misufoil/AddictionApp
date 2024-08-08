package com.example.addictions_edit.presentation.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
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
        modifier = Modifier.padding(8.dp),
        style = AddictionTheme.typography.headlineSmall
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
        modifier = Modifier.padding(8.dp),
        style = AddictionTheme.typography.headlineSmall
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
                    Toast.makeText(
                        context,
                        context.getString(R.string.time_not_come),
                        Toast.LENGTH_SHORT
                    )
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
        modifier = Modifier.padding(8.dp),
        style = AddictionTheme.typography.headlineSmall
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
            .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.large),
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
    val context = LocalContext.current
    var customAddiction by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var showError by remember { mutableStateOf(false) }
    val maxChar = 25

    ModalBottomSheet(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = {
            viewModel.onBottomSheetHide()
        },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = {
            Column {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0
                            showError = false
                        },
                        text = { Text(text = context.getString(R.string.predefined_list)) }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = {
                            selectedTabIndex = 1
                            showError = false
                        },
                        text = { Text(text = context.getString(R.string.custom_input)) }
                    )
                }

                when (selectedTabIndex) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            items(addictionList) { addiction ->
                                Card(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth()
                                        .shadow(
                                            elevation = 1.dp,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .clickable {
                                            coroutineScope.launch {
                                                viewModel.onTypeChange(
                                                    AddictionTypes.getDescription(
                                                        context,
                                                        addiction
                                                    )
                                                )
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
                                            text = AddictionTypes.getDescription(
                                                context,
                                                addiction
                                            ),
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

                    1 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            OutlinedTextField(
                                value = customAddiction,
                                onValueChange = {
                                    if (it.length <= maxChar) {
                                        customAddiction = it
                                        showError = false
                                    }
                                },
                                label = { Text(text = context.getString(R.string.your_addiction)) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                isError = showError,
                                supportingText = {
                                    if (showError) {
                                        Text(
                                            text = context.getString(R.string.enter_custom_addiction),
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Start,
                                        )
                                    } else {
                                        Text(
                                            text = "${customAddiction.length} / $maxChar",
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.End,
                                        )
                                    }
                                },
                            )

                            ElevatedButton(
                                onClick = {
                                    if (customAddiction.isNotBlank()) {
                                        coroutineScope.launch {
                                            viewModel.onTypeChange(customAddiction)
                                            viewModel.onBottomSheetHide()
                                            modalBottomSheetState.hide()
                                        }
                                    } else {
                                        showError = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
//                                colors = ButtonDefaults.elevatedButtonColors(
//                                    containerColor = MaterialTheme.colorScheme.primary,
//                                    contentColor = MaterialTheme.colorScheme.onPrimary
//                                )
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.baseline_save_24),
                                    contentDescription = stringResource(id = R.string.save_button)
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(stringResource(id = R.string.save_button))
                            }
                        }
                    }
                }
            }
        }
    )
}
//TextField(
//    value = text,
//    onValueChange = {
//        if (it.length <= maxChar) text = it
//    },
//    modifier = Modifier.fillMaxWidth(),
//    supportingText = {
//        Text(
//            text = "${text.length} / $maxChar",
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.End,
//        )
//    },
//)

//ElevatedButton(
//            modifier = Modifier.fillMaxWidth().padding(8.dp),
//            onClick = {
//                coroutineScope.launch {
//                    viewModel.saveAddiction()
//                    onPopBackStack()
//                }
//            },
//        ) {
//                            ElevatedButton(
//                                onClick = {
//                                    if (customAddiction.isNotBlank()) {
//                                        coroutineScope.launch {
//                                            viewModel.onTypeChange(customAddiction)
//                                            viewModel.onBottomSheetHide()
//                                            modalBottomSheetState.hide()
//                                        }
//                                    } else {
//                                        Toast.makeText(
//                                            context,
//                                            context.getString(R.string.enter_custom_addiction),
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(8.dp)
//                            ) {
//                                Text(text = context.getString(R.string.use_custom_addiction))
//                            }

//@ExperimentalMaterial3Api
//@Composable
//internal fun ModalBottomSheetComponent(
//    viewModel: AddictionAddEditViewModel,
//    padding: PaddingValues
//) {
//    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//    val addictionList = AddictionTypes.entries
//    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
//
//    ModalBottomSheet(
//        modifier = Modifier.fillMaxSize(),
//        onDismissRequest = {
//            viewModel.onBottomSheetHide()
//        },
//        sheetState = modalBottomSheetState,
//        dragHandle = { BottomSheetDefaults.DragHandle() },
//        content = {
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                items(addictionList) { addiction ->
//                    Card(
//                        modifier = Modifier
//                            .padding(4.dp)
//                            .fillMaxWidth()
//                            .shadow(elevation = 1.dp, shape = MaterialTheme.shapes.medium)
//                            .clickable {
//                                coroutineScope.launch {
//                                    viewModel.onTypeChange(AddictionTypes.getDescription(context, addiction))
//                                    viewModel.onBottomSheetHide()
//                                    modalBottomSheetState.hide()
//                                }
//                            },
//                        colors = CardDefaults.cardColors(
//                            containerColor = AddictionTheme.colorScheme.surfaceVariant,
//                        ),
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = AddictionTypes.getDescription(context, addiction),
//                                modifier = Modifier.padding(
//                                    top = 4.dp,
//                                    bottom = 4.dp,
//                                    start = 10.dp,
//                                    end = 10.dp
//                                ),
//                                fontSize = 24.sp
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    )
//}