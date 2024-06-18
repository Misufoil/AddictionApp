package dev.misufoil.addictions_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.misufoil.addictions.AddictionTheme
import dev.misufoil.addictions_main.models.AddictionUI
import dev.misufoil.core_utils.models.AddictionTypes
import dev.misufoil.core_utils.models.DaysPerWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddictionsMainScreen() {
    AddictionsMainScreen(viewModel = viewModel())
}

@Composable
internal fun AddictionsMainScreen(viewModel: AddictionsMainViewModel) {
    val state by viewModel.state.collectAsState()
    val currentState = state

    if (state != State.None) {
        AddictionsMainContent(currentState)
    }

}

@Composable
private fun AddictionsMainContent(currentState: State) {
    Scaffold(
        floatingActionButton = { Example(onClick = { }) }
    ) {
        Column (
            modifier = Modifier
                .padding(it)
        ){
            if (currentState is State.Error) {
                ErrorMessage(currentState)
            }
            if (currentState is State.Loading) {
                ProgressIndicator(currentState)
            }
            if (currentState.addictions != null) {
                Addictions(addictions = currentState.addictions)
            }
        }
    }


}

@Composable
private fun ErrorMessage(state: State.Error) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(AddictionTheme.colorScheme.error)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error during update", color = AddictionTheme.colorScheme.error)
    }
}

@Composable
private fun ProgressIndicator(state: State.Loading) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun Addictions(
    @PreviewParameter(
        AddictionsPreviewProvider::class,
        limit = 1
    ) addictions: List<AddictionUI>
) {
    LazyColumn {
        items(addictions) { addiction ->
            key(addiction.type) {
                Addiction(addiction)
            }
        }
    }
}

@Preview
@Composable
internal fun Addiction(
    @PreviewParameter(
        AddictionPreviewProvider::class,
        limit = 1
    ) addiction: AddictionUI
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = addiction.type.toString(),
            style = AddictionTheme.typography.headlineMedium,
            maxLines = 1
        )
        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = addiction.date.toString(),
            style = AddictionTheme.typography.bodyMedium,
            maxLines = 1
        )

    }
}

@Composable
fun Example(onClick: () -> Unit) {
    FloatingActionButton(onClick = { onClick() }) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

private class AddictionPreviewProvider : PreviewParameterProvider<AddictionUI> {
    override val values = sequenceOf(
        AddictionUI(
            AddictionTypes.SMOKING,
            LocalDate.now(),
            LocalTime.now(),
            DaysPerWeek.SEVEN,
            20
        ),
        AddictionUI(
            AddictionTypes.ALCOHOL,
            LocalDate.now(),
            LocalTime.now(),
            DaysPerWeek.TWO,
            1
        ),
        AddictionUI(
            AddictionTypes.DRUGS,
            LocalDate.now(),
            LocalTime.now(),
            DaysPerWeek.ONE,
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
