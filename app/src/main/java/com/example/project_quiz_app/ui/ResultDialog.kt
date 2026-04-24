package com.example.project_quiz_app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultDialog(
    score: Int,
    totalQuestions: Int,
    onRestartClick: () -> Unit
) {
    val feedbackMessage = when (score) {
        5 -> "Excellent! \uD83C\uDF89"
        3, 4 -> "Good Job \uD83D\uDC4D"
        else -> "Keep Practicing \uD83D\uDCDA"
    }

    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                text = "Quiz Completed",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Score: $score/$totalQuestions",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = feedbackMessage,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onRestartClick,
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
            ) {
                Text(text = "Restart")
            }
        }
    )
}
