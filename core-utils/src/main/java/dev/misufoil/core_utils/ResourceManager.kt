package dev.misufoil.core_utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceManager @Inject constructor(@ApplicationContext val context: Context) {
    fun getStringById(@StringRes resId: Int, defaultValue: String = "defaultValue"): String {
        return try {
            context.getString(resId)
        } catch (e: Exception) {
            defaultValue
        }
    }
}