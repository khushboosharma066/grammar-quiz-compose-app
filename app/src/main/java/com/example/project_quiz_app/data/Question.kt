package com.example.project_quiz_app.data

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
