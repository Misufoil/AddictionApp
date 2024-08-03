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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.addictions_details.models.AddictionUI
import com.example.addictions_details.presentation.viewmodel.AddictionDetailsViewModel
import dev.misufoil.addictions.CustomCircularProgressIndicator
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.addictions.uikit.R
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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

    Scaffold(
        modifier = Modifier.background(AddictionTheme.colorScheme.surfaceVariant)
    ) { padding ->
        when (currentState) {
            is AddictionDetailsViewModel.State.None -> Unit
            is AddictionDetailsViewModel.State.Error -> ErrorMessage(currentState, padding)
            is AddictionDetailsViewModel.State.Loading -> ProgressIndicator(currentState, padding)
            is AddictionDetailsViewModel.State.Success -> AddictionScreen(
                currentState.addiction,
                viewModel,
                navigateToAddEdit,
                onPopBackStack,
                padding
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

//        ElevatedButton(
//            modifier = Modifier.fillMaxWidth().padding(8.dp),
//            onClick = {
//                navigateToAddEdit(viewModel.state.addiction?.type.toString())
//            },
//            ) {
//            Icon(
//                imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_square_24),
//                contentDescription = stringResource(id = R.string.edit_habit)
//            )
//            Spacer(modifier = Modifier.size(4.dp))
//            Text(text = stringResource(id = R.string.edit_button))
//        }
//
//        ElevatedButton(
//            modifier = Modifier.fillMaxWidth().padding(8.dp),
//            onClick = {
//
//            },
//            colors = ButtonDefaults.elevatedButtonColors(
//                containerColor = MaterialTheme.colorScheme.error,
//                contentColor = MaterialTheme.colorScheme.onError,
//            )
//        ) {
//            Icon(
//                imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_outline_24),
//                contentDescription = stringResource(id = R.string.delete_habit)
//            )
//            Spacer(modifier = Modifier.size(4.dp))
//            Text(text = stringResource(id = R.string.delete_button))
//        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
                    ElevatedButton(
            modifier = Modifier.fillMaxWidth().padding(8.dp).weight(1F),
            onClick = {
                navigateToAddEdit(viewModel.state.addiction?.type.toString())
            },
            ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_square_24),
                contentDescription = stringResource(id = R.string.edit_habit)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = stringResource(id = R.string.edit_button))
        }

        ElevatedButton(
            modifier = Modifier.fillMaxWidth().padding(8.dp).weight(1F),
            onClick = {
                coroutineScope.launch {
                    viewModel.deleteAddiction()
                    onPopBackStack()
                }
            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_outline_24),
                contentDescription = stringResource(id = R.string.delete_habit)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = stringResource(id = R.string.delete_button))
        }

//            AssistChip(
//                onClick = {
//                    navigateToAddEdit(viewModel.state.addiction?.type.toString())
//                },
//                colors = AssistChipDefaults.assistChipColors(
//                    leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
//                ),
//                leadingIcon = {
//                    Icon(
//                        imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_square_24),
//                        contentDescription = stringResource(id = R.string.edit_habit)
//                    )
//                },
//                label = {
//                    Text(text = stringResource(id = R.string.edit_button))
//                }
//            )
//
//            AssistChip(
//                onClick = {
//
//                },
//                colors = AssistChipDefaults.assistChipColors(
//                    leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
//                ),
//                leadingIcon = {
//                    Icon(
//                        imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_square_24),
//                        contentDescription = stringResource(id = R.string.delete_habit)
//                    )
//                },
//                label = {
//                    Text(text = stringResource(id = R.string.delete_button))
//                }
//            )
        }
//        Button(
//            onClick = {
//                navigateToAddEdit(viewModel.state.addiction?.type.toString())
//            },
//            Modifier.fillMaxWidth()
//        ) {
//            Text("Редактировать")
//        }
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
        //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm", Locale.getDefault())
        //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm", Locale.getDefault())
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
                            primaryColor = Color.Green,
                            secondaryColor = Color.LightGray,
                            circleRadius = 300f,
                            textStyleInCircle = MaterialTheme.typography.bodyLarge,
                            textStyleUnderCircle = MaterialTheme.typography.bodyLarge,
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
                            primaryColor = Color.Red,
                            secondaryColor = Color.LightGray,
                            circleRadius = 300f,
                            textStyleInCircle = MaterialTheme.typography.bodyLarge,
                            textStyleUnderCircle = MaterialTheme.typography.bodyLarge,
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
                    if (state.currentPage == iteration) Color.DarkGray else Color.LightGray
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
private fun ErrorMessage(state: AddictionDetailsViewModel.State.Error, padding: PaddingValues) {
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
    state: AddictionDetailsViewModel.State.Loading,
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


