package dev.misufoil.addictions_home.presentation.view

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.addictions_home.models.AddictionUI
import dev.misufoil.addictions_home.presentation.viewmodel.AddictionsMainViewModel
import dev.misufoil.addictions_home.presentation.viewmodel.State
import dev.misufoil.core_utils.CustomCircularProgressIndicator
import dev.misufoil.core_utils.date_time_utils.stringDateTimeToLocalDateTime
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import dev.misufoil.addictions.uikit.R as uikitR


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddictionsHomeScreen(
    modifier: Modifier = Modifier,
    navigateToAddEdit: (String) -> Unit,
    navigateToDetails: (String) -> Unit
) {

    val viewModel: AddictionsMainViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val currentState = state
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = { Example(navigateToAddEdit) }
    ) {
        Column(
            modifier = modifier
                .padding(it)
        ) {
            when (currentState) {
                is State.None -> Unit
                is State.Error -> ErrorMessage(currentState, modifier)
                is State.Loading -> ProgressIndicator(currentState, modifier)
                is State.Success -> AddictionsList(
                    addictionsList = currentState.addictions,
                    deleteAddiction = { addiction: AddictionUI ->
                        viewModel.deleteAddiction(
                            addiction
                        )
                    },
                    deleteWorkerChain = { id: String -> viewModel.deleteWorkerChain(id) },
                    undoDelete = { viewModel.undoDelete() },
                    navigateToDetails = navigateToDetails,
                    navigateToAddEdit = navigateToAddEdit,
                    snackBarHostState = snackBarHostState,
                    modifier = Modifier
                )
            }
        }
    }
}


@Suppress("NonSkippableComposable")
@ExperimentalFoundationApi
@Composable
private fun AddictionsList(
    addictionsList: List<AddictionUI>,
    deleteAddiction: (AddictionUI) -> Unit,
    deleteWorkerChain: (String) -> Unit,
    undoDelete: () -> Unit,
    navigateToDetails: (String) -> Unit,
    navigateToAddEdit: (String) -> Unit,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LazyColumn {
        items(items = addictionsList, key = { item: AddictionUI -> item.id }) { addiction ->
            var boxSize by remember { mutableFloatStateOf(0F) }
            val anchors = DraggableAnchors {
                HorizontalDragValue.Settled at 0f
                HorizontalDragValue.StartToEnd at boxSize / 3
                HorizontalDragValue.EndToStart at -boxSize / 3
            }
            val state = remember {
                AnchoredDraggableState(
                    initialValue = HorizontalDragValue.Settled,
                    positionalThreshold = { distance -> distance * 0.3f },
                    velocityThreshold = { 0.3f },
                    animationSpec = tween(),
                )
            }
            SideEffect { state.updateAnchors(anchors) }

            Card(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .shadow(elevation = 2.dp, shape = MaterialTheme.shapes.large)
                    .animateItemPlacement(),
                colors = CardDefaults.cardColors(
                    containerColor = AddictionTheme.colorScheme.surfaceVariant,
                ),
            ) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(150.dp)
                            .background(AddictionTheme.colorScheme.primaryContainer)
                            .align(Alignment.CenterStart)
                            .clickable {
                                navigateToAddEdit(addiction.id)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            ImageVector.vectorResource(uikitR.drawable.baseline_edit_square_24),
                            contentDescription = stringResource(id = uikitR.string.edit_addiction),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(150.dp)
                            .background(AddictionTheme.colorScheme.errorContainer)
                            .align(Alignment.CenterEnd)
                            .clickable {
                                coroutineScope.launch {
                                    deleteAddiction(addiction)
                                    snackBarHostState
                                        .showSnackbar(
                                            message = context.getString(uikitR.string.addiction_removed),
                                            actionLabel = context.getString(uikitR.string.cancel),
                                            duration = SnackbarDuration.Short
                                        )
                                        .let { result ->
                                            if (result == SnackbarResult.ActionPerformed) {
                                                undoDelete()
                                            } else {
                                                deleteWorkerChain(addiction.type)
                                            }
                                        }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            ImageVector.vectorResource(uikitR.drawable.baseline_delete_outline_24),
                            contentDescription = stringResource(id = uikitR.string.delete_addiction),
                        )

                    }

                    Box(modifier = Modifier
                        .graphicsLayer { boxSize = size.width }
                        .offset {
                            IntOffset(
                                x = state
                                    .requireOffset()
                                    .roundToInt(), y = 0
                            )
                        }
                        .fillMaxWidth()
                        .height(150.dp)
                        .anchoredDraggable(state, Orientation.Horizontal)
                        .background(AddictionTheme.colorScheme.surfaceVariant)

                    ) {
                        Addiction(
                            addiction = addiction,
                            navigateToDetails = navigateToDetails,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun Addiction(
    addiction: AddictionUI,
    navigateToDetails: (String) -> Unit,
    modifier: Modifier
) {
    val addictionDate = stringDateTimeToLocalDateTime(addiction.date, addiction.time)

    Row(
        modifier = modifier.clickable {
            navigateToDetails(addiction.id)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = addiction.type,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 3
            )
            Spacer(modifier = Modifier.size(4.dp))

            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = addiction.date,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2
            )
        }

        CustomCircularProgressIndicator(
            modifier = Modifier
                .padding(2.dp)
                .size(120.dp),
            initialValue = addictionDate,
            circleColor = AddictionTheme.colorScheme.primary,
            secondaryCircleColor = MaterialTheme.colorScheme.secondary,
            circleRadius = 130f,
            textStyleInCircle = MaterialTheme.typography.bodyLarge,
            textStyleUnderCircle = MaterialTheme.typography.bodyLarge,
            smallCircle = true,
            onPositionChange = {}
        )
    }
}

private enum class HorizontalDragValue { Settled, StartToEnd, EndToStart }

@Composable
fun Example(navigateTo: (String) -> Unit) {
    FloatingActionButton(
        onClick = {
            navigateTo("add")
        }
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Floating action button",
            tint = AddictionTheme.colorScheme.onPrimaryContainer
        )
    }
}