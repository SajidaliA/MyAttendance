package com.example.myattendance.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.myattendance.viewmodel.MainViewModel
import com.example.myattendance.ui.theme.HotelAttendanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            //TODO:: Add API call while the splash screen loads
        }
        setContent {
            HotelAttendanceTheme {
                AppMainScreen(mainViewModel = mainViewModel)
            }
        }
    }
}