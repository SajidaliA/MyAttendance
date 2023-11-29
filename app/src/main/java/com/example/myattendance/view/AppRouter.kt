package com.example.myattendance.view

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myattendance.utils.AppScreens
import com.example.myattendance.viewmodel.MainViewModel

/**
 * Created by Sajid Ali Suthar.
 */

@Composable
fun AppRouter(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    openDrawer: () -> Unit
) {
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route) {
        composable(route = AppScreens.AddEditLeave.route + "/{leaveId}/{isEdit}",
            arguments = listOf(
                navArgument("leaveId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("isEdit") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ), enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 1000 }
                )
            }
        ) {
            val isEdit = it.arguments?.getBoolean("isEdit")
            val leaveId = it.arguments?.getString("leaveId")
            AddEditLeaveScreen(navController, mainViewModel, leaveId, isEdit!!)
        }
        composable(route = AppScreens.AddEditAdvance.route + "/{advanceId}/{isEdit}",
            arguments = listOf(
                navArgument("advanceId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("isEdit") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ), enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 1000 }
                )
            }
        ) {
            val isEdit = it.arguments?.getBoolean("isEdit")
            val advanceId = it.arguments?.getString("advanceId")
            AddEditAdvanceScreen(navController, mainViewModel, advanceId, isEdit!!)
        }
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController, mainViewModel)
        }
        composable(route = AppScreens.Account.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 1000 }
                )
            }
        ) {
            AccountScreen(navController, mainViewModel)
        }
        composable(route = AppScreens.LeaveList.route + "/{selectedMonth}",
            arguments = listOf(
                navArgument("selectedMonth") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 1000 }
                )
            },
            popEnterTransition = {
                slideInHorizontally (
                    initialOffsetX = { -1000 }
                )
            }) {
            val selectedMonth = it.arguments?.getString("selectedMonth")
            LeaveListScreen(navController, mainViewModel, selectedMonth)
        }
        composable(route = AppScreens.AdvanceList.route + "/{selectedMonth}",
            arguments = listOf(
                navArgument("selectedMonth") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 }
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 1000 }
                )
            },
            popEnterTransition = {
                slideInHorizontally (
                    initialOffsetX = { -1000 }
                )
            }) {
            val selectedMonth = it.arguments?.getString("selectedMonth")
            AdvanceListScreen(navController, mainViewModel, selectedMonth)
        }
    }
}