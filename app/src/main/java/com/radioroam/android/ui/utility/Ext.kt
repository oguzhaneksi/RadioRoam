package com.radioroam.android.ui.utility

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun String.removeNonAlphanumericFirstChar(): String {
    var output = this
    while (output.isNotEmpty() && !output.first().isLetterOrDigit()) {
        output = output.substring(1)
    }
    return output
}