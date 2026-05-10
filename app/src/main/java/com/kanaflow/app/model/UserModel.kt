package com.kanaflow.app.model

data class UserModel(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val hiraganaProgress: Int = 0,
    val katakanaProgress: Int = 0,
    val kanjiProgress: Int = 0,
    val streak: Int = 0,
    val lastStudied: Long = 0L,
    val createdAt: Long = 0L
)