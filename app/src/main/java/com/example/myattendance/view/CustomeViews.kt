package com.example.myattendance.view

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.example.myattendance.R

/**
 * Created by Sajid Ali Suthar.
 */

@Composable
fun CustomToolbar(title: String, onButtonClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h6
            )
        },
        navigationIcon = {
            IconButton(onClick = { onButtonClicked() }) {
                Icon(Icons.Default.Menu, contentDescription = "Navigation drawer")
            }
        },
        backgroundColor = colorResource(id = R.color.primaryColor),
        contentColor = MaterialTheme.colors.onPrimary,
    )
}

@Composable
fun CustomToolbarWithBackArrow(title: String, navHostController: NavHostController) {
    TopAppBar(title = { Text(text = title, style = MaterialTheme.typography.h6) },
        navigationIcon = {
            IconButton(onClick = { navHostController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
            }
        })

}