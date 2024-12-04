package com.cs407.wellnest

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    val isCompleted: Boolean = false,
    val color: Int = Color(0xFF5BBAE9).toArgb()
)

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos WHERE id = :todoId LIMIT 1")
    fun getTodoByIdFlow(todoId: String): Flow<TodoEntity?>

    @Query("SELECT * FROM todos WHERE category = :category ORDER BY dueDate ASC")
    fun getTodosByCategory(category: Int): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :todoId")
    suspend fun getTodoById(todoId: String): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("UPDATE todos SET isCompleted = :isCompleted WHERE id = :todoId")
    suspend fun updateTodoCompletion(todoId: String, isCompleted: Boolean)
}

// Migration strategy
val MIGRATION_1_2 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE your_table ADD COLUMN new_column_name TEXT")
    }
}

@Database(entities = [TodoEntity::class], version = 3)
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
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}