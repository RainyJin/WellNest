package com.cs407.wellnest

import androidx.room.*
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import java.util.*

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val dueDate: String,
    val category: Int, // 0 for Academics, 1 for Health
    val repeatOption: String,
    val isCompleted: Boolean = false
)

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos WHERE category = :category ORDER BY dueDate ASC")
    fun getTodosByCategory(category: Int): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("UPDATE todos SET isCompleted = :isCompleted WHERE id = :todoId")
    suspend fun updateTodoCompletion(todoId: String, isCompleted: Boolean)
}

@Database(entities = [TodoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}