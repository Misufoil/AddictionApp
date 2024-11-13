package com.example.activity_api

import android.content.Context
import android.content.Intent

interface MainActivityIntentRouter {
    fun launch(context: Context, id: String): Intent
}