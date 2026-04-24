package com.example.project_quiz_app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.project_quiz_app.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel
) {
    // Collecting StateFlow as Compose state: whenever uiState changes,
    // this composable recomposes and updates the visible UI automatically.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentQuestion = uiState.currentQuestion ?: return
    val totalQuestions = uiState.questions.size.coerceAtLeast(1)
    val progress = (uiState.currentQuestionIndex + 1).toFloat() / totalQuestions
    val nextEnabled = uiState.selectedAnswerIndex != null && !uiState.showAnswerFeedback
    val buttonAlpha by animateFloatAsState(
        targetValue = if (nextEnabled) 1f else 0.65f,
        animationSpec = spring(),
        label = "nextButtonAlpha"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Grammar Quiz App",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Text(
                text = "Question ${uiState.currentQuestionIndex + 1}/${uiState.questions.size}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = currentQuestion.questionText,
                        style = MaterialTheme.typography.titleMedium
                    )

                    currentQuestion.options.forEachIndexed { index, option ->
                        // UI-only mapping: derive colors from ViewModel state.
                        // Business rules remain inside the ViewModel.
                        val isCorrect = index == currentQuestion.correctAnswerIndex
                        val isSelected = uiState.selectedAnswerIndex == index
                        val optionColor = when {
                            uiState.showAnswerFeedback && isCorrect -> Color(0xFF2E7D32)
                            uiState.showAnswerFeedback && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                        val optionBackgroundTarget = when {
                            uiState.showAnswerFeedback && isCorrect -> Color(0xFFE8F5E9)
                            uiState.showAnswerFeedback && isSelected && !isCorrect -> Color(0xFFFFEBEE)
                            isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                            else -> Color.Transparent
                        }
                        // Animate background color for subtle and professional transitions.
                        val optionBackgroundColor by animateColorAsState(
                            targetValue = optionBackgroundTarget,
                            label = "optionBackgroundColor"
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = optionBackgroundColor),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.selectAnswer(index) },
                                        enabled = !uiState.showAnswerFeedback,
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = when {
                                                uiState.showAnswerFeedback && isCorrect -> Color(0xFF2E7D32)
                                                uiState.showAnswerFeedback && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                                                else -> MaterialTheme.colorScheme.primary
                                            }
                                        )
                                    )
                                    Text(
                                        text = option,
                                        modifier = Modifier.padding(top = 12.dp),
                                        color = optionColor,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.goToNextQuestion() },
                enabled = nextEnabled,
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp, pressedElevation = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(buttonAlpha)
            ) {
                Text(text = "Next")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }

    if (uiState.showResultDialog) {
        ResultDialog(
            score = uiState.score,
            totalQuestions = uiState.questions.size,
            onRestartClick = { viewModel.restartQuiz() }
        )
    }
}
