package com.cs407.wellnest.data

import java.time.LocalDate
import androidx.room.*
import androidx.room.RoomDatabase
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity(tableName = "countdowns")
data class CountdownEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val targetDate: String,
    val description: String,
    val repeatOption: String)

@Dao
interface CountdownDao {
    @Query("SELECT * FROM countdowns ORDER BY targetDate ASC")
    suspend fun getCountdownItems(): List<CountdownEntity>

    @Upsert
    suspend fun upsertCountdown(countdown: CountdownEntity)

    @Delete
    suspend fun deleteCountdown(countdown: CountdownEntity)

    @Query("DELETE FROM countdowns WHERE targetDate < :today")
    suspend fun deleteExpiredCountdown(
        today: String = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
    )
}

@Database(entities = [CountdownEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countdownDao(): CountdownDao
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