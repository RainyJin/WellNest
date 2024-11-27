package com.cs407.wellnest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val todoDao = database.todoDao()

    fun getTodosByCategory(category: Int): Flow<List<TodoEntity>> {
        return todoDao.getTodosByCategory(category)
    }

    suspend fun saveTodo(todo: TodoEntity) {
        todoDao.insertTodo(todo)
    }

    suspend fun updateTodo(todo: TodoEntity) {
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }

    suspend fun updateTodoCompletion(todoId: String, isCompleted: Boolean) {
        todoDao.updateTodoCompletion(todoId, isCompleted)
    }
}