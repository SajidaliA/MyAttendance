package com.example.myattendance.utils

import com.example.myattendance.R

/**
 * Created by Sajid Ali Suthar.
 */

sealed class AppScreens(val title: String, val route: String, var icon: Int) {
    data object HomeScreen : AppScreens("Home", "homeScreen", R.drawable.ic_home)
    data object AddEditLeave : AppScreens("Add/Edit Leave", "addEditLeave", R.drawable.ic_calender)
    data object AddEditAdvance : AppScreens("Add/Edit Advance", "addEditAdvance", R.drawable.ic_money)
    data object Account : AppScreens("Account", "account", R.drawable.ic_account)
    data object LeaveList : AppScreens("Leave List", "leaveList", R.drawable.ic_calender)
    data object AdvanceList : AppScreens("Advance List", "advanceList", R.drawable.ic_money)

    fun routeWithArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg -> append("/$arg") }
        }
    }
}
