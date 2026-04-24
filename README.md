# Grammar Quiz App

An offline Android quiz app that helps users practice basic English grammar through 5 multiple-choice questions.  
Built with **Kotlin**, **Jetpack Compose**, and **MVVM** for clear separation of concerns and beginner-friendly code.

## Project Overview

The app shows one question at a time, lets the user select an answer, provides brief correct/wrong visual feedback, and then moves to the next question.  
At the end, a result dialog displays the final score and allows restarting the quiz.

This project is designed as a clean internship assignment submission: simple, readable, and easy to explain in a walkthrough.

## Features

- Offline quiz with fake local repository data
- Exactly 5 grammar MCQ questions
- One-question-at-a-time quiz experience
- Progress UI:
  - Question count text (`Question X/5`)
  - Linear progress indicator
- Option selection using radio buttons
- Selected option highlighting
- Correct/wrong feedback highlighting before next question
- Next button disabled until answer is selected
- Final result dialog with score and feedback message
- Restart flow with proper state reset
- Lightweight UI animations for smoother interaction

## Tech Stack

- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose
- **Design System:** Material 3
- **Architecture:** MVVM (Model-View-ViewModel)
- **State Management:** `StateFlow`
- **Data Source:** Fake repository (offline static data)
- **Async Handling:** Kotlin Coroutines

## Architecture Explanation

This app follows MVVM:

- **Model (`data/`)**
  - `Question.kt` defines quiz question structure.
  - `QuizRepository.kt` provides question data.
- **ViewModel (`viewmodel/`)**
  - `QuizViewModel.kt` stores quiz state and business logic.
  - Calculates score, controls feedback timing, handles navigation/restart state.
- **View (`ui/`)**
  - `QuizScreen.kt` renders UI based on state from ViewModel.
  - `ResultDialog.kt` displays final result UI.

### MVVM Flow in this app

1. `QuizRepository` returns question list.
2. `QuizViewModel` exposes `uiState` as `StateFlow`.
3. Compose UI collects `uiState`.
4. User action (select/next/restart) triggers ViewModel function.
5. ViewModel updates state.
6. Compose recomposes automatically with new state.

## Folder Structure

```text
app/src/main/java/com/example/project_quiz_app/
├── data/
│   ├── Question.kt
│   └── QuizRepository.kt
├── ui/
│   ├── QuizScreen.kt
│   ├── ResultDialog.kt
│   └── theme/
├── viewmodel/
│   └── QuizViewModel.kt
└── MainActivity.kt
```

## How to Run the App

1. Open the project in **Android Studio**.
2. Let Gradle sync completely.
3. Select an emulator or physical Android device.
4. Run the `app` configuration.
5. Complete quiz and test restart flow.

## Screenshots

screenshot_app1.jpeg
screenshot_app2.jpeg

- Home/Quiz question screen
- Option feedback state (correct/wrong highlight)
- Final result dialog



