package dev.misufoil.addictions_home.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.misufoil.addictions.CustomCircularProgressIndicator
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.addictions_home.models.AddictionUI
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal fun Addiction(
    addiction: AddictionUI,
    navigateToDetails: (Int) -> Unit,
    modifier: Modifier
) {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val date = LocalDate.parse(addiction.date, dateFormatter)
    val time = LocalTime.parse(addiction.time, timeFormatter)
    val addictionDate = LocalDateTime.of(date, time)

    Row(
        modifier = modifier.clickable {
            addiction.id?.let { navigateToDetails(it) }
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
                style = AddictionTheme.typography.headlineLarge,
                maxLines = 3
            )
            Spacer(modifier = Modifier.size(4.dp))

            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = addiction.date,
                style = AddictionTheme.typography.headlineSmall,
                maxLines = 2
            )
        }

        CustomCircularProgressIndicator(
            modifier = Modifier
                .padding(2.dp)
                .size(120.dp),
            initialValue = addictionDate,
            circleColor = AddictionTheme.colorScheme.primary,
            secondaryCircleColor = AddictionTheme.colorScheme.secondary,
            circleRadius = 130f,
            textStyleInCircle = AddictionTheme.typography.bodyLarge,
            textStyleUnderCircle = AddictionTheme.typography.bodyLarge,
            smallCircle = true,
            onPositionChange = {}
        )
    }
}

@Composable
fun FAB(navigateTo: (Int) -> Unit) {
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