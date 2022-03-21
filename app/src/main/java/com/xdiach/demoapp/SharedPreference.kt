package com.xdiach.demoapp

import android.content.Context

class SharedPreference(context: Context) {

    val preferenceName = "Shared_Preference_App"
    val preferenceSort = "Preference_Store"

    val preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    fun getSortType() : Boolean {
        return preference.getBoolean(preferenceSort, false)
    }

    fun setSortType(sort: Boolean) {
        val editor = preference.edit()
        editor.putBoolean(preferenceSort, sort)
        editor.apply()
    }
}