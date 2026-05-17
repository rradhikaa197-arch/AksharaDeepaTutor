package com.aksharadeep.tutor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapters")
data class Chapter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val name: String,
    val isCompleted: Boolean = false
)