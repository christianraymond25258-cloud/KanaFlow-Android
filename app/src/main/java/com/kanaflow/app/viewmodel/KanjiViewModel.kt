package com.kanaflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanaflow.app.model.KanjiCard
import com.kanaflow.app.model.KanjiData
import com.kanaflow.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class KanjiAnswerState { IDLE, CORRECT, WRONG }

data class KanjiSessionState(
    val currentCard: KanjiCard? = null,
    val currentIndex: Int = 0,
    val totalCards: Int = 0,
    val options: List<String> = emptyList(),
    val selectedOption: String? = null,
    val answerState: KanjiAnswerState = KanjiAnswerState.IDLE,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val isFinished: Boolean = false,
    val wrongAnswers: List<Pair<KanjiCard, String>> = emptyList(),
    val progressSaved: Boolean = false
)

class KanjiViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _sessionState = MutableStateFlow(KanjiSessionState())
    val sessionState: StateFlow<KanjiSessionState> = _sessionState

    private var sessionCards: List<KanjiCard> = emptyList()
    private var allCards: List<KanjiCard> = emptyList()
    private var currentWrongAnswers = mutableListOf<Pair<KanjiCard, String>>()

    fun startSession(levelIndices: List<Int>) {
        allCards = KanjiData.allLevels
            .filterIndexed { index, _ -> levelIndices.contains(index) }
            .flatMap { it.second }

        sessionCards = allCards.shuffled()
        currentWrongAnswers = mutableListOf()

        if (sessionCards.isNotEmpty()) {
            _sessionState.value = KanjiSessionState(
                currentCard = sessionCards.first(),
                currentIndex = 0,
                totalCards = sessionCards.size,
                options = generateOptions(sessionCards.first())
            )
        }
    }

    private fun generateOptions(correct: KanjiCard): List<String> {
        val correctAnswer = correct.meaning
        val wrongOptions = allCards
            .filter { it.kanji != correct.kanji }
            .shuffled()
            .take(3)
            .map { it.meaning }
        return (wrongOptions + correctAnswer).shuffled()
    }

    fun selectAnswer(selected: String) {
        val state = _sessionState.value
        val current = state.currentCard ?: return
        if (state.answerState != KanjiAnswerState.IDLE) return

        val isCorrect = selected == current.meaning

        if (!isCorrect) {
            currentWrongAnswers.add(Pair(current, selected))
        }

        _sessionState.value = state.copy(
            selectedOption = selected,
            answerState = if (isCorrect) KanjiAnswerState.CORRECT else KanjiAnswerState.WRONG,
            correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount,
            wrongCount = if (!isCorrect) state.wrongCount + 1 else state.wrongCount
        )
    }

    fun nextCard() {
        val state = _sessionState.value
        val nextIndex = state.currentIndex + 1

        if (nextIndex >= sessionCards.size) {
            _sessionState.value = state.copy(
                isFinished = true,
                wrongAnswers = currentWrongAnswers.toList()
            )
            saveProgress()
        } else {
            val nextCard = sessionCards[nextIndex]
            _sessionState.value = state.copy(
                currentCard = nextCard,
                currentIndex = nextIndex,
                options = generateOptions(nextCard),
                selectedOption = null,
                answerState = KanjiAnswerState.IDLE
            )
        }
    }

    private fun saveProgress() {
        viewModelScope.launch {
            try {
                val state = _sessionState.value
                val total = state.correctCount + state.wrongCount
                val score = if (total > 0) (state.correctCount * 100) / total else 0
                repository.saveKanjiProgress(score)
                repository.updateStreak()
                _sessionState.value = _sessionState.value.copy(progressSaved = true)
            } catch (e: Exception) { }
        }
    }

    fun resetSession() {
        _sessionState.value = KanjiSessionState()
        sessionCards = emptyList()
        currentWrongAnswers = mutableListOf()
    }
}