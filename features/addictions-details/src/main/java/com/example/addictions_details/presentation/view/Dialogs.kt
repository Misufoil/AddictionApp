package com.example.addictions_details.presentation.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.misufoil.addictions.uikit.R

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = R.string.delete_quation)) },
        confirmButton = {
            ElevatedButton(
                onClick = { onConfirm() },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                )
            ) {
                Text(text = stringResource(id = R.string.delete_button))
            }
        },
        dismissButton = {
            ElevatedButton(
                onClick = { onDismiss() }
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}