package dev.misufoil.addictions_home.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.misufoil.addictions_home.models.AddictionUI
import dev.misufoil.core_utils.models.AddictionTypes
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
    navigateToAddEdit: (String) -> Unit,
    navigateToDetails: (String) -> Unit
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
    navigateToAddEdit: (String) -> Unit,
    navigateToDetails: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val currentState = state
    AddictionsHomeContent(currentState, modifier, navigateToAddEdit, navigateToDetails)
}

@Composable
private fun AddictionsHomeContent(
    currentState: State,
    modifier: Modifier = Modifier,
    navigateToAddEdit: (String) -> Unit,
    navigateToDetails: (String) -> Unit
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
                is State.Error -> ErrorMessage(currentState)
                is State.Loading -> ProgressIndicator(currentState)
                is State.Success -> Addictions(
                    addictions = currentState.addictions,
                    navigateToDetails
                )
            }
        }
    }
}

@Composable
private fun ErrorMessage(state: State.Error) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error during update")
    }
}

@Composable
private fun ProgressIndicator(state: State.Loading) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Addictions(
    @PreviewParameter(
        AddictionsPreviewProvider::class,
        limit = 1
    ) addictions: List<AddictionUI>,
    navigateToDetails: (String) -> Unit
) {
    LazyColumn {
        items(addictions) { addiction ->
            key(addiction.type) {
                Addiction(addiction, navigateToDetails)
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
    navigateToDetails: (String) -> Unit
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

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable {
                navigateToDetails(addiction.type.description)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
                .fillMaxWidth()
                .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
                .clickable {
                    navigateToDetails(addiction.type.description)
                }
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = addiction.type.toString(),
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(4.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = addiction.date,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
        }

        CustomCircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp)
                .size(100.dp),
            initialValue = addictionDate,
            primaryColor = Color.Green,
            secondaryColor = Color.LightGray,
            circleRadius = 100f,
            onPositionChange = {}
        )
    }
}


//        CustomCircularProgressIndicator(
//
//        )
//    }

@Composable
fun Example(navigateTo: (String) -> Unit) {
    FloatingActionButton(
        onClick = {

            navigateTo("Add")
        }
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

private class AddictionPreviewProvider : PreviewParameterProvider<AddictionUI> {
    override val values = sequenceOf(
        AddictionUI(
            AddictionTypes.SMOKING,
            "LocalDate.now()",
            "LocalTime.now()",
            3,
            20
        ),
        AddictionUI(
            AddictionTypes.ALCOHOL,
            "LocalDate.now()",
            "LocalTime.now()",
            2,
            1
        ),
        AddictionUI(
            AddictionTypes.DRUGS,
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
