package com.example.project_quiz_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.project_quiz_app.data.QuizRepository
import com.example.project_quiz_app.ui.QuizScreen
import com.example.project_quiz_app.ui.theme.Project_quiz_appTheme
import com.example.project_quiz_app.viewmodel.QuizViewModel
import com.example.project_quiz_app.viewmodel.QuizViewModelFactory

class MainActivity : ComponentActivity() {
    private val quizViewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(QuizRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Project_quiz_appTheme {
                QuizScreen(viewModel = quizViewModel)
            }
        }
    }
}