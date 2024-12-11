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
                val distance = steps * 0.8 // Example: 0.8 meters per step
                val calories = steps * 0.05 // Example: 0.05 kcal per step
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
        val account = GoogleSignIn.getLastSignedInAccount(context) ?: return
        val endTime = System.currentTimeMillis()
        val startTime = endTime - TimeUnit.DAYS.toMillis(7) // Last 7 days

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        Fitness.getHistoryClient(context, account)
            .readData(readRequest)
            .addOnSuccessListener { response ->
                val totalSteps = response.buckets.sumOf { bucket ->
                    bucket.dataSets.firstOrNull()?.dataPoints?.firstOrNull()
                        ?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0
                }
                val distance = totalSteps * 0.8 // Example: 0.8 meters per step
                val calories = totalSteps * 0.05 // Example: 0.05 kcal per step
                onSuccess(totalSteps, distance, calories)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun fetchMonthlyData(
        onSuccess: (Int, Double, Double) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val account = GoogleSignIn.getLastSignedInAccount(context) ?: return
        val endTime = System.currentTimeMillis()
        val startTime = endTime - TimeUnit.DAYS.toMillis(30) // Last 30 days

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        Fitness.getHistoryClient(context, account)
            .readData(readRequest)
            .addOnSuccessListener { response ->
                val totalSteps = response.buckets.sumOf { bucket ->
                    bucket.dataSets.firstOrNull()?.dataPoints?.firstOrNull()
                        ?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0
                }
                val distance = totalSteps * 0.8 // Example: 0.8 meters per step
                val calories = totalSteps * 0.05 // Example: 0.05 kcal per step
                onSuccess(totalSteps, distance, calories)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}
