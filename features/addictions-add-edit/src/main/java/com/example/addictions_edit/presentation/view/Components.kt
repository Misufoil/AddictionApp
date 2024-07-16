package com.example.addictions_edit.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addictions_edit.presentation.viewmodel.AddictionAddEditViewModel
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.addictions.uikit.R
import dev.misufoil.core_utils.models.AddictionTypes
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
internal fun TypeComponent(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = "Зависимость",
        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
        style = AddictionTheme.typography.titleLarge
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable(
                onClick = onClick
                //showBottomSheet = true
            )
    ) {
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


@ExperimentalMaterial3Api
@Composable
internal fun DateAndTimeComponent(viewModel: AddictionAddEditViewModel) {
    Text(
        text = "Дата и время начала",
        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
        style = AddictionTheme.typography.titleLarge
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable(
                onClick = {
                    viewModel.showDateDialogShow()
                }
            )
    ) {
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable(
                onClick = { viewModel.showTimeDialogShow() }
                //showBottomSheet = true
            )
    ) {
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

    if (viewModel.showDateDialog) {
        DatePickerDialog(viewModel)
    }

    if (viewModel.showTimeDialog) {
        TimePickerDialog(viewModel)
    }
}


@ExperimentalMaterial3Api
@Composable
internal fun FrequencyOfUseComponent(viewModel: AddictionAddEditViewModel) {
    Text(
        text = "Использование до отслеживания",
        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
        style = AddictionTheme.typography.titleLarge
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = "Дней в неделю: ",
            modifier = Modifier.padding(16.dp).weight(1f),
            style = AddictionTheme.typography.bodyLarge
        )

        IconButton(
            onClick = { if (viewModel.daysPerWeek > 1) viewModel.onDaysPerWeekChange(viewModel.daysPerWeek - 1) },
            enabled = viewModel.daysPerWeek > 1
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_remove_24),
                contentDescription = "Уменьшить количество дней",
                tint = if (viewModel.daysPerWeek > 1) Color.Green else Color.DarkGray
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
                contentDescription = "Увеличить количество дней",
                tint = if (viewModel.daysPerWeek < 7) Color.Green else Color.DarkGray
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = "Кол-во раз в день: ",
            modifier = Modifier.padding(16.dp).weight(1f),
            style = AddictionTheme.typography.bodyLarge
        )

        IconButton(
            onClick = { if (viewModel.timesInDay > 1) viewModel.onTimesInDayChange(viewModel.timesInDay - 1) },
            enabled = viewModel.timesInDay > 1
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_remove_24),
                contentDescription = "Уменьшить количество дней",
                tint = if (viewModel.timesInDay > 1) Color.Green else Color.DarkGray
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
                contentDescription = "Увеличить количество дней",
                tint = if (viewModel.timesInDay < 100) Color.Green else Color.DarkGray
            )
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
            Card(
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(addictionList) { addiction ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    coroutineScope.launch {
                                        viewModel.onTypeChange(addiction)
                                        viewModel.onBottomSheetHide()
                                        modalBottomSheetState.hide()
                                    }
                                }) {
                            Text(
                                text = addiction.description,
                                modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        }
    )
}