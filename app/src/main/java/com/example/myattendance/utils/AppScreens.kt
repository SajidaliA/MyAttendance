package com.example.myattendance.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import com.example.myattendance.R

/**
 * Created by Sajid Ali Suthar.
 */

sealed class AppScreens(val title: String, val route: String, var icon: Int) {
    data object HomeScreen : AppScreens("Home", "homeScreen", R.drawable.ic_home)
    data object AddEditLeave : AppScreens("Add/Edit Leave", "addEditLeave", R.drawable.leave_icon)
    data object LeaveDetails : AppScreens("Leave details", "leaveDetails", R.drawable.leave_icon)
    data object AddEditAdvance : AppScreens("Add/Edit Advance", "addEditAdvance", R.drawable.advance_icon)
    data object AdvanceDetails : AppScreens("Advance details", "advanceDetails", R.drawable.advance_icon)
    data object Account : AppScreens("Account", "account", R.drawable.ic_account)
    data object UserDetail : AppScreens("User Details", "userDetails", R.drawable.ic_account)
    data object LeaveList : AppScreens("Leave List", "leaveList", R.drawable.leave_icon)
    data object AdvanceList : AppScreens("Advance List", "advanceList", R.drawable.advance_icon)

    fun routeWithArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg -> append("/$arg") }
        }
    }
}
