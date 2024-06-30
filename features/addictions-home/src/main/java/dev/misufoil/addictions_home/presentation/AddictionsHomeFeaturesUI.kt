package dev.misufoil.addictions_home.presentation

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.misufoil.addictions_home.models.AddictionUI
import dev.misufoil.core_utils.models.AddictionTypes
import dev.misufoil.core_utils.models.DaysPerWeek
import java.time.LocalDate
import java.time.LocalTime

//fun AddictionsHomeScreen(onItemClick: () -> Unit, addButtonClick: () -> Unit


@Composable
fun AddictionsHomeScreen(onItemClick: () -> Unit, addButtonClick: () -> Unit) {
    AddictionsHomeScreen(viewModel = viewModel())
}

@Composable
internal fun AddictionsHomeScreen(viewModel: AddictionsMainViewModel) {
    val state by viewModel.state.collectAsState()
    val currentState = state

    if (state != State.None) {
        AddictionsHomeContent(currentState)
    }

}

@Composable
private fun AddictionsHomeContent(currentState: State) {
    Scaffold(
        floatingActionButton = { Example() }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
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
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Enter text") },
        maxLines = 2,
        textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(20.dp)
    )


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
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1
        )
        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = addiction.date.toString(),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )

    }
}

@Composable
fun Example() {
    FloatingActionButton(
        onClick = {
//            navController.navigate(Screen)
        }
    ) {
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
