package com.example.myattendance.view

import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.example.myattendance.viewmodel.MainViewModel
import kotlinx.coroutines.launch

/**
 * Created by Sajid Ali Suthar.
 */
@Composable
fun AppMainScreen(mainViewModel: MainViewModel) {

    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }

        ModalDrawer (
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                Drawer(
                    onDestinationClicked = { route ->
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) {
            AppRouter(navController = navController, mainViewModel = mainViewModel, openDrawer = {
                openDrawer()
            })
        }
    }
}