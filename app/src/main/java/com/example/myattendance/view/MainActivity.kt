package com.example.myattendance.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.myattendance.viewmodel.MainViewModel
import com.example.myattendance.ui.theme.HotelAttendanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    constructor(name: String) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HotelAttendanceTheme {
                AppMainScreen(mainViewModel = mainViewModel)
            }
        }
    }
}