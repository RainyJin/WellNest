package com.cs407.wellnest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

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

    suspend fun completeTodo(todo: TodoEntity) {
        // If the todo does not repeat, simply mark as completed
        if (todo.repeatOption == "Does not repeat") {
            updateTodoCompletion(todo.id, true)
            return
        }

        // For recurring todos, mark current instance as completed
        updateTodoCompletion(todo.id, true)

        // Create a new todo instance based on the repeat pattern
        val newTodo = todo.copy(
            id = generateUniqueId(), // Generate a new unique ID
            dueDate = when (todo.repeatOption) {
                "Daily" -> LocalDate.parse(todo.dueDate).plusDays(1).toString()
                "Weekly" -> LocalDate.parse(todo.dueDate).plusWeeks(1).toString()
                "Monthly" -> LocalDate.parse(todo.dueDate).plusMonths(1).toString()
                else -> todo.dueDate
            },
            isCompleted = false // New instance starts as not completed
        )

        // Insert the new todo
        saveTodo(newTodo)
    }

    // Helper method to generate a unique ID
    private fun generateUniqueId(): String {
        return java.util.UUID.randomUUID().toString()
    }
}