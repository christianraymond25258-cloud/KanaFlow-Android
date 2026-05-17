package com.jikana.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    init {
        // Check if user is already logged in
        _authState.value = AuthState(user = auth.currentUser)
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState(error = "Please fill in all fields")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState(
                    user = auth.currentUser,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _authState.value = AuthState(
                    error = e.message ?: "Login failed. Please try again."
                )
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _authState.value = AuthState(error = "Please fill in all fields")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState(error = "Password must be at least 6 characters")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                // Save user profile to Firestore
                auth.currentUser?.let { user ->
                    firestore.collection("users").document(user.uid).set(
                        mapOf(
                            "name" to name,
                            "email" to email,
                            "createdAt" to System.currentTimeMillis(),
                            "hiraganaProgress" to 0,
                            "katakanaProgress" to 0,
                            "kanjiProgress" to 0,
                            "streak" to 0
                        )
                    ).await()
                }
                _authState.value = AuthState(
                    user = auth.currentUser,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _authState.value = AuthState(
                    error = e.message ?: "Registration failed. Please try again."
                )
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState()
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}