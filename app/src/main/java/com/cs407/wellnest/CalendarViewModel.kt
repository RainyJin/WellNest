package com.cs407.wellnest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cs407.wellnest.data.AppDatabase1
import com.cs407.wellnest.data.CountdownEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase1.getDatabase(application)
    private val countdownDao = database.countdownDao()

    init {
        // remove expired countdown items
        viewModelScope.launch {
            deleteExpiredCountdown()
        }
    }

    fun getCountdownItemsFlow(): Flow<List<CountdownEntity>> {
        return countdownDao.getCountdownItemsFlow()
    }

    suspend fun getCountdownItems(): List<CountdownEntity> {
        return countdownDao.getCountdownItems()
    }

    suspend fun getCountdownByIdAndDate(id: String, targetDate: String): CountdownEntity? {
        return countdownDao.getCountdownByIdAndDate(id, targetDate)
    }

    suspend fun insertCountdown(countdown: CountdownEntity) {
        return countdownDao.insertCountdown(countdown)
    }

    suspend fun updateCountdown(countdown: CountdownEntity) {
        return countdownDao.updateCountdown(countdown)
    }

    suspend fun deleteCountdown(id: String) {
        return countdownDao.deleteCountdown(id)
    }

    suspend fun deleteExpiredCountdown() {
        return countdownDao.deleteExpiredCountdown()
    }
}