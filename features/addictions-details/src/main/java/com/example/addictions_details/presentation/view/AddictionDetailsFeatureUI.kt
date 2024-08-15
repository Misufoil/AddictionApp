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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.addictions_details.models.AddictionUI
import com.example.addictions_details.presentation.viewmodel.AddictionDetailsViewModel
import com.example.addictions_details.presentation.viewmodel.State
import dev.misufoil.addictions.CustomCircularProgressIndicator
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
    AddictionAddEditScreen(
        viewModel = hiltViewModel(),
        navigateToAddEdit = navigateToAddEdit,
        onPopBackStack = onPopBackStack,
        modifier = modifier
    )
}

@ExperimentalFoundationApi
@Composable
private fun AddictionAddEditScreen(
    viewModel: AddictionDetailsViewModel,
    navigateToAddEdit: (String) -> Unit,
    onPopBackStack: () -> Boolean,
    modifier: Modifier
) {
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
                currentState.addiction,
                viewModel,
                navigateToAddEdit,
                onPopBackStack,
                padding
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
    viewModel: AddictionDetailsViewModel,
    navigateToAddEdit: (String) -> Unit,
    onPopBackStack: () -> Boolean,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val coroutineScope = rememberCoroutineScope()

        InfoSlider(addiction)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ElevatedButton(
                modifier = Modifier.fillMaxWidth().padding(8.dp).weight(1F),
                onClick = {
                    navigateToAddEdit(viewModel.state.addiction?.id.toString())
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
                        viewModel.showDeleteDialog()
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

@ExperimentalFoundationApi
@Composable
internal fun InfoSlider(addiction: AddictionUI) {
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
        ) { page: Int ->
            when (page) {
                0 -> {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .background(
                                color = AddictionTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                        //colors = CardDefaults.cardColors(
                        //            containerColor = AddictionTheme.colorScheme.surfaceVariant,
                        //        ),
                        //        shape = MaterialTheme.shapes.large
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

                1 -> {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .background(
                                color = AddictionTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
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


@Composable
private fun ErrorMessage(state: State.Error, padding: PaddingValues) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error during update ${state.addiction?.type}")
    }
}

@Composable
private fun ProgressIndicator(
    state: State.Loading,
    padding: PaddingValues
) {
    Column(modifier = Modifier.padding(padding)) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
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

