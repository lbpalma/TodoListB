package com.example.todolistb


data class Task(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String? = null,
    val isDone: Boolean = false
)