package com.cs407.wellnest.utils

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.concurrent.TimeUnit

class GoogleFitHelper(private val context: Context) {

    fun hasGoogleFitPermissions(): Boolean {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .build()

        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account != null && GoogleSignIn.hasPermissions(account, fitnessOptions)
    }

    fun requestGoogleFitPermissions(activity: ComponentActivity, requestCode: Int) {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .build()

        GoogleSignIn.requestPermissions(
            activity,
            requestCode,
            GoogleSignIn.getAccountForExtension(context, fitnessOptions),
            fitnessOptions
        )
    }

    fun fetchDailyData(
        onSuccess: (Int, Double, Double) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val account = GoogleSignIn.getLastSignedInAccount(context) ?: return
        Fitness.getHistoryClient(context, account)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                val steps = dataSet.dataPoints.firstOrNull()
                    ?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0
                val distance = steps * 0.8
                val calories = steps * 0.05
                onSuccess(steps, distance, calories)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun fetchWeeklyData(
        onSuccess: (Int, Double, Double) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account == null) {
            onFailure(Exception("Google Account not signed in"))
            return
        }

        // Fetch the latest day's data
        Fitness.getHistoryClient(context, account)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                val dailySteps = dataSet.dataPoints.firstOrNull()
                    ?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0

                // Generate a random weekly step count between 35,000 and 70,000
                val randomWeeklySteps = (35000..70000).random()

                // Add some variability influenced by the daily steps
                val weeklySteps = (randomWeeklySteps + dailySteps * (0..2).random()).coerceIn(35000, 70000)

                // Calculate distance and calories based on the guessed weekly steps
                val weeklyDistance = weeklySteps * 0.8
                val weeklyCalories = weeklySteps * 0.05

                Log.d(
                    "FetchWeeklyData",
                    "Guessed Weekly Steps: $weeklySteps, Distance: $weeklyDistance, Calories: $weeklyCalories"
                )
                onSuccess(weeklySteps, weeklyDistance, weeklyCalories)
            }
            .addOnFailureListener { exception ->
                Log.e("FetchWeeklyData", "Failed to fetch daily data for weekly guess: ${exception.message}")
                onFailure(exception)
            }
    }






    fun fetchMonthlyData(
        onSuccess: (Int, Double, Double) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account == null) {
            onFailure(Exception("Google Account not signed in"))
            return
        }

        // Fetch the latest day's data
        Fitness.getHistoryClient(context, account)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                val dailySteps = dataSet.dataPoints.firstOrNull()
                    ?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0

                val randomMonthlySteps = (150000..300000).random()

                val monthlySteps = (randomMonthlySteps + dailySteps * (0..10).random())
                    .coerceIn(150000, 300000)

                // Calculate distance and calories based on the guessed monthly steps
                val monthlyDistance = monthlySteps * 0.8
                val monthlyCalories = monthlySteps * 0.05

                Log.d(
                    "FetchMonthlyData",
                    "Generated Monthly Steps: $monthlySteps, Distance: $monthlyDistance, Calories: $monthlyCalories"
                )
                onSuccess(monthlySteps, monthlyDistance, monthlyCalories)
            }
            .addOnFailureListener { exception ->
                Log.e("FetchMonthlyData", "Failed to fetch daily data for monthly guess: ${exception.message}")
                onFailure(exception)
            }
    }




}
