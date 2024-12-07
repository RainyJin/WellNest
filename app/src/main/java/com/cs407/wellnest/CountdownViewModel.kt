package com.cs407.wellnest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cs407.wellnest.data.AppDatabase1
import com.cs407.wellnest.data.CountdownEntity
import kotlinx.coroutines.launch

class CountdownViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase1.getDatabase(application)
    private val countdownDao = database.countdownDao()

    init {
        // remove expired countdown items
        viewModelScope.launch {
            deleteExpiredCountdown()
        }
    }

    suspend fun getCountdownItems(): List<CountdownEntity> {
        return countdownDao.getCountdownItems()
    }

    suspend fun getCountdownByIdAndDate(id: String, targetDate: String): CountdownEntity? {
        return countdownDao.getCountdownByIdAndDate(id, targetDate)
    }


    suspend fun upsertCountdown(countdown: CountdownEntity) {
        return countdownDao.upsertCountdown(countdown)
    }

    suspend fun deleteCountdown(countdown: CountdownEntity) {
        return countdownDao.deleteCountdown(countdown)
    }

    suspend fun deleteExpiredCountdown() {
        return countdownDao.deleteExpiredCountdown()
    }
}