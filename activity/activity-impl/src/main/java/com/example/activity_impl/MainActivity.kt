package com.example.activity_impl

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.example.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import dev.misufoil.addictions.theme.AddictionTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val params = intent.getStringExtra("addictionId") ?: ""


        enableEdgeToEdge()
        setContent {
            AddictionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    AppNavGraph(navController = rememberNavController(), startParams = params,)
                }
            }
        }

        requestNotificationPermission()
    }

    private fun requestNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    companion object {
        const val REQUEST_NOTIFICATION_PERMISSION_CODE = 1001

        fun newInstance(context: Context, id: String): Intent {
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("addictionId", id)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            return intent
        }
    }
}