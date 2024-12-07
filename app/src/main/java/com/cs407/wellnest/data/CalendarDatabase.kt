package com.cs407.wellnest.data

import java.time.LocalDate
import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity(tableName = "countdowns", primaryKeys = ["id", "targetDate"])
data class CountdownEntity(
    val id: String = UUID.randomUUID().toString(),
    val targetDate: String,
    val description: String,
    val repeatOption: String,
    val endDate: String? = null // only for repeating events
    )

@Dao
interface CountdownDao {
    @Query("SELECT * FROM countdowns ORDER BY targetDate ASC")
    suspend fun getCountdownItems(): List<CountdownEntity>

    @Query("SELECT * FROM countdowns WHERE id = :id AND targetDate = :targetDate")
    suspend fun getCountdownByIdAndDate(id: String, targetDate: String): CountdownEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCountdown(countdown: CountdownEntity)

    @Delete
    suspend fun deleteCountdown(countdown: CountdownEntity)

    @Query("DELETE FROM countdowns WHERE targetDate < :today")
    suspend fun deleteExpiredCountdown(
        today: String = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
    )
}

@Database(entities = [CountdownEntity::class], version = 4)
abstract class AppDatabase1 : RoomDatabase() {
    abstract fun countdownDao(): CountdownDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase1? = null
        fun getDatabase(context: android.content.Context): AppDatabase1 {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase1::class.java,
                    "app_database1"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}