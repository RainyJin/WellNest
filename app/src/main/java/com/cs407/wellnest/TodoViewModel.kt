package com.cs407.wellnest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val todoDao = database.todoDao()

    fun getTodosByCategory(category: Int): Flow<List<TodoEntity>> {
        println("Getting todos for category: $category")
        return todoDao.getTodosByCategory(category)
    }

    suspend fun saveTodo(todo: TodoEntity) {
        println("Saving todo: $todo.id")
        todoDao.insertTodo(todo)
        println("Todo saved successfully")
    }

    suspend fun updateTodo(todo: TodoEntity) {
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }

    suspend fun getTodoById(todoId: String): TodoEntity? {
        println("Attempting to get todo with ID: $todoId")
        val todo = todoDao.getTodoById(todoId)
        println("Retrieved todo: $todo")
        return todo
    }

    fun getTodoByIdFlow(todoId: String): Flow<TodoEntity?> {
        return todoDao.getTodoByIdFlow(todoId)
    }

    suspend fun updateTodoCompletion(todoId: String, isCompleted: Boolean) {
        todoDao.updateTodoCompletion(todoId, isCompleted)
    }
}