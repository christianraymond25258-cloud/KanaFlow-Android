package com.jikana.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jikana.app.model.AnswerState
import com.jikana.app.model.KanaChar
import com.jikana.app.model.KanaRow
import com.jikana.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PracticeState(
    val currentChar: KanaChar? = null,
    val currentIndex: Int = 0,
    val totalChars: Int = 0,
    val input: String = "",
    val answerState: AnswerState = AnswerState.IDLE,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val isFinished: Boolean = false,
    val wrongAnswers: List<Pair<KanaChar, String>> = emptyList(),
    val progressSaved: Boolean = false
)

class KanaViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val repository = UserRepository()

    private val _practiceState = MutableStateFlow(PracticeState())
    val practiceState: StateFlow<PracticeState> = _practiceState

    private var practiceList: List<KanaChar> = emptyList()
    private var currentWrongAnswers = mutableListOf<Pair<KanaChar, String>>()
    private var selectedRowCount = 0

    fun startPractice(rows: List<KanaRow>) {
        practiceList = rows.flatMap { it.characters }.shuffled()
        currentWrongAnswers = mutableListOf()
        selectedRowCount = rows.size

        _practiceState.value = PracticeState(
            currentChar = practiceList.firstOrNull(),
            currentIndex = 0,
            totalChars = practiceList.size,
            isFinished = practiceList.isEmpty()
        )
    }

    fun onInputChanged(input: String) {
        _practiceState.value = _practiceState.value.copy(
            input = input,
            answerState = AnswerState.IDLE
        )
    }

    fun submitAnswer() {
        val state = _practiceState.value
        val current = state.currentChar ?: return
        val userInput = state.input.trim().lowercase()
        val correct = current.romaji.lowercase()
        val isCorrect = userInput == correct

        if (!isCorrect) {
            currentWrongAnswers.add(Pair(current, userInput))
        }

        _practiceState.value = state.copy(
            answerState = if (isCorrect) AnswerState.CORRECT else AnswerState.WRONG,
            correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount,
            wrongCount = if (!isCorrect) state.wrongCount + 1 else state.wrongCount
        )
    }

    fun nextCharacter() {
        val state = _practiceState.value
        val nextIndex = state.currentIndex + 1
        if (nextIndex >= practiceList.size) {
            _practiceState.value = state.copy(
                isFinished = true,
                wrongAnswers = currentWrongAnswers.toList()
            )
        } else {
            _practiceState.value = state.copy(
                currentChar = practiceList[nextIndex],
                currentIndex = nextIndex,
                input = "",
                answerState = AnswerState.IDLE
            )
        }
    }

    fun saveProgress(progressField: String, currentProgress: Int) {
        viewModelScope.launch {
            try {
                val total = _practiceState.value.correctCount +
                        _practiceState.value.wrongCount
                val score = if (total > 0)
                    (_practiceState.value.correctCount * 100) / total else 0

                // Only update if score is good (above 70%)
                val newProgress = if (score >= 70) {
                    (currentProgress + selectedRowCount).coerceAtMost(10)
                } else {
                    currentProgress
                }

                repository.saveKanaProgress(progressField, newProgress)
                repository.updateStreak()

                _practiceState.value = _practiceState.value.copy(
                    progressSaved = true
                )
            } catch (e: Exception) { }
        }
    }

    fun resetPractice() {
        _practiceState.value = PracticeState()
        practiceList = emptyList()
        currentWrongAnswers = mutableListOf()
        selectedRowCount = 0
    }
}