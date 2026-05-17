package com.aksharadeep.tutor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chapterId: Int,
    val subject: String,
    val score: Int,
    val totalQuestions: Int = 5,
    val date: Long = System.currentTimeMillis()
)