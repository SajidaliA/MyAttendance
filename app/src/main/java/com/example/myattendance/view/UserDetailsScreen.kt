package com.example.myattendance.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.myattendance.database.User

/**
 * Created by Sajid Ali Suthar.
 */

@Composable
fun UserDetailsScreen(
    navHostController: NavHostController,
    user: User
) {
    Surface {
        Column {
            Row {
                Text(text = "Name")
                Text(text = user.name)
            }
        }
    }
}