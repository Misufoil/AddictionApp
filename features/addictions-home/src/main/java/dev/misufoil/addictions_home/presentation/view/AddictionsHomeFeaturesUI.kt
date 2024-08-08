package dev.misufoil.addictions_home.presentation.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.misufoil.addictions.CustomCircularProgressIndicator
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.addictions_home.models.AddictionUI
import dev.misufoil.addictions_home.presentation.viewmodel.AddictionsMainViewModel
import dev.misufoil.addictions_home.presentation.viewmodel.State
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

//fun AddictionsHomeScreen(onItemClick: () -> Unit, addButtonClick: () -> Unit

//onItemClick: () -> Unit, addButtonClick: () -> Unit
@Composable
fun AddictionsHomeScreen(
    modifier: Modifier = Modifier,
    navigateToAddEdit: (Int) -> Unit,
    navigateToDetails: (Int) -> Unit
) {
    AddictionsHomeScreen(
        viewModel = hiltViewModel(),
        modifier = modifier,
        navigateToAddEdit,
        navigateToDetails
    )
}

@Composable
internal fun AddictionsHomeScreen(
    viewModel: AddictionsMainViewModel,
    modifier: Modifier = Modifier,
    navigateToAddEdit: (Int) -> Unit,
    navigateToDetails: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val currentState = state
    AddictionsHomeContent(currentState, modifier, navigateToAddEdit, navigateToDetails)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddictionsHomeContent(
    currentState: State,
    modifier: Modifier = Modifier,
    navigateToAddEdit: (Int) -> Unit,
    navigateToDetails: (Int) -> Unit
) {
    Scaffold(
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
                is State.Success -> Addictions(
                    addictions = currentState.addictions,
                    navigateToDetails,
                    modifier
                )
            }
        }
    }
}

@Composable
private fun ErrorMessage(state: State.Error, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error during update")
    }
}

@Composable
private fun ProgressIndicator(state: State.Loading, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@ExperimentalFoundationApi
@Composable
private fun Addictions(
    @PreviewParameter(
        AddictionsPreviewProvider::class,
        limit = 1
    ) addictions: List<AddictionUI>,
    navigateToDetails: (Int) -> Unit,
    modifier: Modifier
) {
    LazyColumn {
        items(addictions) { addiction ->
            key(addiction.type) {
                Addiction(addiction, navigateToDetails, modifier)
            }
        }
    }
}

@Composable
internal fun Addiction(
    @PreviewParameter(
        AddictionPreviewProvider::class,
        limit = 1
    ) addiction: AddictionUI,
    navigateToDetails: (Int) -> Unit,
    modifier: Modifier
) {
    // Измените форматтер, чтобы он соответствовал формату addiction.date
    // val addictionDate = LocalDateTime.parse("${addiction.date}T00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
//    val date = addiction.date
//    val time = addiction.time
//    /val dateTimeString = "${addiction.date} ${addiction.time}"

    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm", Locale.getDefault())
    //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm", Locale.getDefault())
    val date = LocalDate.parse(addiction.date, dateFormatter)
    val time = LocalTime.parse(addiction.time, timeFormatter)


    val addictionDate = LocalDateTime.of(date, time)
//    val addictionDate = LocalDateTime.parse(dateTimeString, dateTimeFormatter)
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                addiction.id?.let { navigateToDetails(it) }
            }
            .shadow(elevation = 2.dp, shape = MaterialTheme.shapes.large),
        colors = CardDefaults.cardColors(
            containerColor = AddictionTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
//                    .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
//                    .clickable {
//                        navigateToDetails(addiction.type.description)
//                    }
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    text = addiction.type,
                    style = MaterialTheme.typography.headlineLarge,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    text = addiction.date,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2
                )
            }

            CustomCircularProgressIndicator(
                modifier = Modifier
                    .padding(4.dp)
                    .size(150.dp),
                initialValue = addictionDate,
                circleColor = AddictionTheme.colorScheme.primary,
                secondaryCircleColor = MaterialTheme.colorScheme.secondary,
                circleRadius = 150f,
                textStyleInCircle = MaterialTheme.typography.bodyLarge,
                textStyleUnderCircle = MaterialTheme.typography.bodyLarge,
                smallCircle = true,
                onPositionChange = {}
            )
        }
    }
}

private enum class HorizontalDragValue { Settled, StartToEnd, EndToStart }

//@Composable
//private fun Addictions(
//    @PreviewParameter(
//        AddictionsPreviewProvider::class,
//        limit = 1
//    ) addictions: List<AddictionUI>,
//    navigateToDetails: (Int) -> Unit,
//    modifier: Modifier
//) {
//    LazyColumn {
//        items(addictions) { addiction ->
//            key(addiction.type) {
//                Addiction(addiction, navigateToDetails, modifier)
//            }
//        }
//    }
//}
//
//@Composable
//internal fun Addiction(
//    @PreviewParameter(
//        AddictionPreviewProvider::class,
//        limit = 1
//    ) addiction: AddictionUI,
//    navigateToDetails: (Int) -> Unit,
//    modifier: Modifier
//) {
//    // Измените форматтер, чтобы он соответствовал формату addiction.date
//    // val addictionDate = LocalDateTime.parse("${addiction.date}T00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
////    val date = addiction.date
////    val time = addiction.time
////    /val dateTimeString = "${addiction.date} ${addiction.time}"
//
//    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
//    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
//    //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm", Locale.getDefault())
//    //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm", Locale.getDefault())
//    val date = LocalDate.parse(addiction.date, dateFormatter)
//    val time = LocalTime.parse(addiction.time, timeFormatter)
//
//
//    val addictionDate = LocalDateTime.of(date, time)
////    val addictionDate = LocalDateTime.parse(dateTimeString, dateTimeFormatter)
//    Card(
//        modifier = modifier
//            .padding(8.dp)
//            .fillMaxWidth()
//            .clickable {
//                addiction.id?.let { navigateToDetails(it) }
//            }
//            .shadow(elevation = 2.dp, shape = MaterialTheme.shapes.large),
//        colors = CardDefaults.cardColors(
//            containerColor = AddictionTheme.colorScheme.surfaceVariant,
//        ),
//    ) {
//        Row(
//            modifier = modifier,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(8.dp)
//                    .weight(1f)
//                    .fillMaxWidth()
////                    .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
////                    .clickable {
////                        navigateToDetails(addiction.type.description)
////                    }
//            ) {
//                Text(
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
//                    text = addiction.type,
//                    style = MaterialTheme.typography.headlineLarge,
//                    maxLines = 3
//                )
//                Spacer(modifier = Modifier.size(4.dp))
//
//                Text(
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
//                    text = addiction.date,
//                    style = MaterialTheme.typography.headlineSmall,
//                    maxLines = 2
//                )
//            }
//
//            CustomCircularProgressIndicator(
//                modifier = Modifier
//                    .padding(4.dp)
//                    .size(150.dp),
//                initialValue = addictionDate,
//                circleColor = AddictionTheme.colorScheme.primary,
//                secondaryСircleColor = MaterialTheme.colorScheme.secondary,
//                circleRadius = 150f,
//                textStyleInCircle = MaterialTheme.typography.bodyLarge,
//                textStyleUnderCircle = MaterialTheme.typography.bodyLarge,
//                smallCircle = true,
//                onPositionChange = {}
//            )
//        }
//    }
//}

@Composable
fun Example(navigateTo: (Int) -> Unit) {
    FloatingActionButton(
        onClick = {
            navigateTo(-1)
        }
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Floating action button",
            tint = AddictionTheme.colorScheme.onPrimaryContainer
        )
    }
}

private class AddictionPreviewProvider : PreviewParameterProvider<AddictionUI> {
    override val values = sequenceOf(
        AddictionUI(
            id = 1,
            "stringResource(id = s)",
            "LocalDate.now()",
            "LocalTime.now()",
            3,
            20
        ),
        AddictionUI(
            id = 2,
            "AddictionTypes.ALCOHOL",
            "LocalDate.now()",
            "LocalTime.now()",
            2,
            1
        ),
        AddictionUI(
            id = 3,
            "AddictionTypes.DRUGS",
            "LocalDate.now()",
            "LocalTime.now()",
            1,
            1
        )
    )

}

private class AddictionsPreviewProvider : PreviewParameterProvider<List<AddictionUI>> {

    private val addictionProvider = AddictionPreviewProvider()

    override val values = sequenceOf(
        addictionProvider.values.toList()
    )

}
