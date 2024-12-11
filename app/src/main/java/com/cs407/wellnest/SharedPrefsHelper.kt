package com.cs407.wellnest

import android.content.Context

object SharedPrefsHelper {
    private const val PREFS_NAME = "WellnestPrefs"

    // Save an integer value
    fun saveInt(context: Context, key: String, value: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }

    // Retrieve an integer value
    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(key, defaultValue)
    }

    // Save a float value
    fun saveFloat(context: Context, key: String, value: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(key, value).apply()
    }

    // Retrieve a float value
    fun getFloat(context: Context, key: String, defaultValue: Float = 0f): Float {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(key, defaultValue)
    }

    // Save a string value
    fun saveString(context: Context, key: String, value: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(key, value).apply()
    }

    // Retrieve a string value
    fun getString(context: Context, key: String, defaultValue: String = ""): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    // Save a boolean value
    fun saveBoolean(context: Context, key: String, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, value).apply()
    }

    // Retrieve a boolean value
    fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, defaultValue)
    }

    // Remove a key
    fun remove(context: Context, key: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(key).apply()
    }

    // Clear all preferences
    fun clear(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
