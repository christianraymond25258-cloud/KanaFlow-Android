package com.kanaflow.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kanaflow.app.model.UserModel
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getCurrentUser(): UserModel? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            UserModel(
                uid = uid,
                name = doc.getString("name") ?: "",
                email = doc.getString("email") ?: "",
                hiraganaProgress = (doc.getLong("hiraganaProgress") ?: 0L).toInt(),
                katakanaProgress = (doc.getLong("katakanaProgress") ?: 0L).toInt(),
                kanjiProgress = (doc.getLong("kanjiProgress") ?: 0L).toInt(),
                streak = (doc.getLong("streak") ?: 0L).toInt(),
                lastStudied = doc.getLong("lastStudied") ?: 0L,
                createdAt = doc.getLong("createdAt") ?: 0L
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveKanaProgress(field: String, rowsCompleted: Int) {
        val uid = auth.currentUser?.uid ?: return
        try {
            firestore.collection("users").document(uid)
                .update(
                    mapOf(
                        field to rowsCompleted,
                        "lastStudied" to System.currentTimeMillis()
                    )
                ).await()
        } catch (e: Exception) { }
    }

    suspend fun saveKanjiProgress(score: Int) {
        val uid = auth.currentUser?.uid ?: return
        try {
            firestore.collection("users").document(uid)
                .update(
                    mapOf(
                        "kanjiProgress" to score,
                        "lastStudied" to System.currentTimeMillis()
                    )
                ).await()
        } catch (e: Exception) { }
    }

    suspend fun updateStreak(): Int {
        val uid = auth.currentUser?.uid ?: return 0
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            val lastStudied = doc.getLong("lastStudied") ?: 0L
            val currentStreak = (doc.getLong("streak") ?: 0L).toInt()

            val now = Calendar.getInstance()
            val last = Calendar.getInstance().apply {
                timeInMillis = lastStudied
            }

            val daysDiff = ((now.timeInMillis - last.timeInMillis) /
                    (1000 * 60 * 60 * 24)).toInt()

            val newStreak = when {
                lastStudied == 0L -> 1
                daysDiff == 0 -> currentStreak
                daysDiff == 1 -> currentStreak + 1
                else -> 1
            }

            firestore.collection("users").document(uid)
                .update(
                    mapOf(
                        "streak" to newStreak,
                        "lastStudied" to System.currentTimeMillis()
                    )
                ).await()

            newStreak
        } catch (e: Exception) {
            0
        }
    }
}