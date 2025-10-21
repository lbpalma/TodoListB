package com.example.todolistb



import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.todolistb.Task

class TaskViewModel : ViewModel() {

    // Estado observable por Compose
    val tasks = mutableStateListOf<Task>()

    fun addTask(title: String, description: String?) {
        val newTask = Task(title = title.trim(), description = description?.trim())
        tasks.add(0, newTask) // al inicio
    }

    fun toggleTaskDone(id: Long, done: Boolean) {
        val idx = tasks.indexOfFirst { it.id == id }
        if (idx != -1) {
            tasks[idx] = tasks[idx].copy(isDone = done)
        }
    }

    fun removeTask(id: Long) {
        tasks.removeAll { it.id == id }
    }
}