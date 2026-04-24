package com.example.project_quiz_app.data

class QuizRepository {
    // Repository is the single source of quiz data for the ViewModel.
    // For this assignment, data is static and offline (fake repository).
    // In production this layer could be replaced by Room, API, or both.
    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                questionText = "Choose the correct sentence:",
                options = listOf(
                    "She don't like coffee.",
                    "She doesn't likes coffee.",
                    "She doesn't like coffee.",
                    "She not like coffee."
                ),
                correctAnswerIndex = 2
            ),
            Question(
                questionText = "Fill in the blank: They ___ going to school now.",
                options = listOf("is", "are", "am", "be"),
                correctAnswerIndex = 1
            ),
            Question(
                questionText = "Which word is an adjective in this sentence: 'The quick fox jumps high'?",
                options = listOf("fox", "jumps", "quick", "high"),
                correctAnswerIndex = 2
            ),
            Question(
                questionText = "Select the correct past tense form of 'go':",
                options = listOf("goed", "went", "gone", "going"),
                correctAnswerIndex = 1
            ),
            Question(
                questionText = "Identify the correct punctuation:",
                options = listOf(
                    "Lets eat, Grandma!",
                    "Let's eat Grandma!",
                    "Lets eat Grandma!",
                    "Let's eat, Grandma!"
                ),
                correctAnswerIndex = 3
            )
        )
    }
}
