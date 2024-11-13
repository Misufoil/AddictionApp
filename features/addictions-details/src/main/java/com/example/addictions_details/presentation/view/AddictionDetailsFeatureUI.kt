package com.example.addictions_details.presentation.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.addictions_details.models.AddictionUI
import com.example.addictions_details.presentation.viewmodel.AddictionDetailsViewModel
import com.example.addictions_details.presentation.viewmodel.State
import dev.misufoil.core_utils.CustomCircularProgressIndicator
import dev.misufoil.addictions.theme.AddictionTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
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
                padding = padding
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
    padding: PaddingValues
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val coroutineScope = rememberCoroutineScope()

        InfoSlider(addiction)
        SavingsScreen()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ElevatedButton(
                modifier = Modifier.fillMaxWidth().padding(8.dp).weight(1F),
                onClick = {
                    navigateToAddEdit(addiction.id)
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

@Composable
fun SavingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(uikitR.string.overall_saving),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AddictionTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(uikitR.string.money_saved),
                        style = AddictionTheme.typography.titleMedium
                    )
                    Text(
                        text = "2 970,00 ₽",
                        style = AddictionTheme.typography.titleMedium,
                        color = AddictionTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().size(1.dp).background(MaterialTheme.colorScheme.onSurface))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(uikitR.string.calories_saved),
                        style = AddictionTheme.typography.titleMedium
                    )
                    Text(
                        text = "2970",
                        style = AddictionTheme.typography.titleMedium,
                        color = AddictionTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@ExperimentalFoundationApi
@Composable
@Preview
internal fun InfoSlider(@PreviewParameter(AddictionUIPreviewParameterProvider::class) addiction: AddictionUI) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val date = LocalDate.parse(addiction.date, dateFormatter)
        val time = LocalTime.parse(addiction.time, timeFormatter)


        val addictionDate = LocalDateTime.of(date, time)

        val state = rememberPagerState(pageCount = { 2 })

        HorizontalPager(
            state = state,
            beyondBoundsPageCount = 1,
            modifier = Modifier.fillMaxWidth()
                .background(color = AddictionTheme.colorScheme.surfaceVariant)
        ) { page: Int ->
            when (page) {
                0 -> {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .background(
                                color = AddictionTheme.colorScheme.surfaceVariant,
                            ),
                        contentAlignment = Alignment.Center

                    ) {
                        Column(
                            modifier = Modifier.size(368.dp).padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                stringResource(id = uikitR.string.start_sober),
                                modifier = Modifier.padding(vertical = 16.dp),
                                style = MaterialTheme.typography.titleLarge,
                                color = AddictionTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.padding(20.dp))
                            Text(
                                addiction.date,
                                style = MaterialTheme.typography.headlineMedium,
                                color = AddictionTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(
                                addiction.time,
                                style = MaterialTheme.typography.headlineSmall,
                                color = AddictionTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                1 -> {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .background(
                                color = AddictionTheme.colorScheme.surfaceVariant,
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomCircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(300.dp),
                            initialValue = addictionDate,
                            circleColor = AddictionTheme.colorScheme.primary,
                            secondaryCircleColor = MaterialTheme.colorScheme.secondary,
                            circleRadius = 300f,
                            textStyleInCircle = MaterialTheme.typography.headlineMedium,
                            textStyleUnderCircle = MaterialTheme.typography.headlineMedium,
                            smallCircle = false,
                            onPositionChange = {}
                        )
                    }
                }
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .background(color = AddictionTheme.colorScheme.surfaceVariant)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(state.pageCount) { iteration ->
                val color =
                    if (state.currentPage == iteration) AddictionTheme.colorScheme.primary else AddictionTheme.colorScheme.secondary
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }
}

class AddictionUIPreviewParameterProvider : PreviewParameterProvider<AddictionUI> {
    override val values: Sequence<AddictionUI> = sequenceOf(
        AddictionUI(
            id = "222",
            type = "Example Type",
            date = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
            time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
            daysPerWeek = 3,
            timesInDay = 2
        )
    )
}


@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = uikitR.string.delete_quation)) },
        confirmButton = {
            ElevatedButton(
                onClick = { onConfirm() },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                )
            ) {
                Text(text = stringResource(id = uikitR.string.delete_button))
            }
        },
        dismissButton = {
            ElevatedButton(
                onClick = { onDismiss() }
            ) {
                Text(text = stringResource(id = uikitR.string.cancel))
            }
        }
    )
}

