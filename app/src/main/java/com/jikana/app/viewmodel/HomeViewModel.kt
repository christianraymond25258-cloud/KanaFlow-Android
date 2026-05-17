package com.jikana.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jikana.app.model.UserModel
import com.jikana.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val isLoading: Boolean = true,
    val user: UserModel? = null,
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            _homeState.value = HomeState(isLoading = true)
            val user = repository.getCurrentUser()
            _homeState.value = HomeState(
                isLoading = false,
                user = user,
                error = if (user == null) "Failed to load profile" else null
            )
        }
    }

    fun refreshUser() {
        loadUser()
    }
}