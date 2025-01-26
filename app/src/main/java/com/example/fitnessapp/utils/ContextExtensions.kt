package com.example.fitnessapp.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

// Extension function to retrieve the Activity from a Context
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}
