package com.cs407.wellnest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cs407.wellnest.data.AppDatabase
import com.cs407.wellnest.data.CountdownEntity
import kotlinx.coroutines.launch

class CountdownViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
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

    suspend fun insertCountdown(countdown: CountdownEntity) {
        return countdownDao.insertCountdown(countdown)
    }

    suspend fun updateCountdown(countdown: CountdownEntity) {
        return countdownDao.updateCountdown(countdown)
    }

    suspend fun deleteCountdown(countdown: CountdownEntity) {
        return countdownDao.deleteCountdown(countdown)
    }

    suspend fun deleteExpiredCountdown() {
        return countdownDao.deleteExpiredCountdown()
    }
}