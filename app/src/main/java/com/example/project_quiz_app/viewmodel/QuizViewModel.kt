package com.example.project_quiz_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project_quiz_app.data.Question
import com.example.project_quiz_app.data.QuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val score: Int = 0,
    val showResultDialog: Boolean = false,
    val showAnswerFeedback: Boolean = false
) {
    // Helper getter used by UI to safely read the active question.
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)
}

class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    // ViewModel holds quiz state and business rules.
    // This keeps composables simple and survives configuration changes (like rotation).
    private val _uiState = MutableStateFlow(
        QuizUiState(questions = repository.getQuestions())
    )
    // UI collects this immutable stream; each emitted state triggers recomposition.
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    private var nextQuestionJob: Job? = null

    fun selectAnswer(answerIndex: Int) {
        _uiState.update { currentState ->
            if (currentState.showAnswerFeedback) return@update currentState
            currentState.copy(selectedAnswerIndex = answerIndex)
        }
    }

    fun goToNextQuestion() {
        val snapshot = _uiState.value
        if (snapshot.showAnswerFeedback) return
        val selectedAnswer = snapshot.selectedAnswerIndex ?: return
        val currentQuestion = snapshot.currentQuestion ?: return

        // Score is computed in ViewModel so UI stays "display-only".
        val updatedScore = if (selectedAnswer == currentQuestion.correctAnswerIndex) {
            snapshot.score + 1
        } else {
            snapshot.score
        }

        // Show green/red feedback briefly before moving forward.
        _uiState.update { currentState ->
            currentState.copy(
                score = updatedScore,
                showAnswerFeedback = true
            )
        }

        nextQuestionJob?.cancel()
        nextQuestionJob = viewModelScope.launch {
            // Brief delay so users can see correct/wrong feedback colors.
            delay(700)
            _uiState.update { currentState ->
                val isLastQuestion = currentState.currentQuestionIndex == currentState.questions.lastIndex
                if (isLastQuestion) {
                    currentState.copy(
                        showAnswerFeedback = false,
                        showResultDialog = true
                    )
                } else {
                    currentState.copy(
                        currentQuestionIndex = currentState.currentQuestionIndex + 1,
                        selectedAnswerIndex = null,
                        showAnswerFeedback = false
                    )
                }
            }
        }
    }

    fun restartQuiz() {
        nextQuestionJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                currentQuestionIndex = 0,
                selectedAnswerIndex = null,
                score = 0,
                showResultDialog = false,
                showAnswerFeedback = false
            )
        }
    }
}

class QuizViewModelFactory(
    private val repository: QuizRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
