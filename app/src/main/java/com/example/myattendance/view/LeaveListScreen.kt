package com.example.myattendance.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myattendance.R
import com.example.myattendance.database.Leave
import com.example.myattendance.utils.AppScreens
import com.example.myattendance.utils.SINGLE_DAY
import com.example.myattendance.utils.montserratFontFamily
import com.example.myattendance.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by Sajid Ali Suthar.
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LeaveListScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    selectedMonth: String?,
) {
    val c = Calendar.getInstance()
    val yearNum = SimpleDateFormat("yyyy", Locale.ENGLISH)
    val currentYear = yearNum.format(c.time)
    if (selectedMonth == currentYear) {
        mainViewModel.getAllLeaves()
    } else {
        mainViewModel.getLeaveByMonth(selectedMonth?.take(3).toString())
    }

    val leavesByMonth: List<Leave> by mainViewModel.leavesByMonth.observeAsState(initial = listOf())
    val leaves: List<Leave> by mainViewModel.allLeaves.observeAsState(initial = listOf())
    val lazyListState = rememberLazyListState()

    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var deleteCanceled by remember { mutableStateOf(false) }
    var leaveToDelete by remember { mutableStateOf(Leave()) }

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colorResource(id = R.color.background)
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.gStart),
                                    colorResource(id = R.color.gEnd)
                                )
                            ),
                            shape = RoundedCornerShape(bottomEnd = 30.dp)
                        ),
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            modifier = Modifier
                                .size(65.dp)
                                .padding(20.dp)
                                .clickable { navHostController.popBackStack() },
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                Color.White
                            )
                        )

                        androidx.compose.material.Text(
                            modifier = Modifier.padding(20.dp),
                            text = "$selectedMonth Leaves",
                            textAlign = TextAlign.Start,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = montserratFontFamily,
                            fontWeight = FontWeight.Normal,
                        )
                    }

                }

                if ((selectedMonth == currentYear && leaves.isNotEmpty()) || leavesByMonth.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(),
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        items(
                            items = if (selectedMonth == currentYear) {
                                leaves
                            } else {
                                leavesByMonth
                            }
                        ) { leave ->
                            val dismissState = rememberDismissState(
                                confirmStateChange = { value ->
                                    if (value == DismissValue.DismissedToStart) {
                                        leaveToDelete = leave
                                        deleteCanceled = false
                                        showDeleteConfirmationDialog = true

                                    }
                                    if (value == DismissValue.DismissedToEnd) {
                                        navHostController.navigate(AppScreens.AddEditLeave.route + "/" + leave.id + "/" + true)
                                    }
                                    true
                                }
                            )
                            if (dismissState.currentValue != DismissValue.Default && (deleteCanceled || dismissState.dismissDirection == DismissDirection.StartToEnd)) {
                                LaunchedEffect(Unit) {
                                    dismissState.reset()
                                }
                            }

                            SwipeToDismiss(state = dismissState, background = {
                                val color = when (dismissState.dismissDirection) {
                                    DismissDirection.EndToStart -> Color.Red
                                    DismissDirection.StartToEnd -> colorResource(id = R.color.gStart)
                                    else -> Color.Transparent
                                }

                                Box(
                                    modifier = Modifier
                                        .background(
                                            shape = RoundedCornerShape(20.dp),
                                            color = color
                                        )
                                        .fillMaxSize()
                                        .padding(vertical = 15.dp, horizontal = 15.dp)
                                ) {
                                    if (dismissState.dismissDirection == DismissDirection.EndToStart){
                                        SwipeIcon( modifier = Modifier
                                            .align(
                                                Alignment.CenterEnd
                                            )
                                            .size(35.dp), R.drawable.ic_delete, "Delete")
                                    }
                                    if (dismissState.dismissDirection == DismissDirection.StartToEnd){
                                        SwipeIcon( modifier = Modifier
                                            .align(
                                                Alignment.CenterStart
                                            )
                                            .size(30.dp), R.drawable.ic_edit, "Edit")
                                    }
                                }
                            }, dismissContent = {
                                LeaveCard(leave, navHostController)
                            })

                        }
                    }

                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No any leave added.",
                            fontSize = 20.sp,
                            fontFamily = montserratFontFamily,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }


            }

        }
    )

    if (showDeleteConfirmationDialog) {
        var updateData by remember { mutableStateOf(false) }
        ConfirmationDialog(
            "Delete confirm!",
            "Are you sure you want to delete this leave?",
            onConfirmation = {
                mainViewModel.deleteLeave(leaveToDelete)
                updateData = true
                showDeleteConfirmationDialog = false
            }, onDismissRequest = {
                deleteCanceled = true
                showDeleteConfirmationDialog = false
            })

        LaunchedEffect(updateData) {
            if (selectedMonth == currentYear) {
                mainViewModel.getAllLeaves()
            } else {
                mainViewModel.getLeaveByMonth(selectedMonth?.take(3).toString())
            }
        }
    }

}

@Composable
fun SwipeIcon(modifier: Modifier, icon: Int, text: String) {
    Icon(
        painter = painterResource(icon),
        contentDescription = text,
        modifier = modifier,
        tint = colorResource(id = R.color.white)
    )
}

@Composable
fun ConfirmationDialog(
    title: String,
    text: String,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit
) {


    AlertDialog(
        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
        title = { Text(text = title) },
        text = { Text(text = text) },
        onDismissRequest = { },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation()
            }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismissRequest()
            }) {
                Text(text = "Cancel")
            }
        })

}


@Composable
fun LeaveCard(
    leave: Leave,
    navHostController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                20.dp,
                RoundedCornerShape(20.dp),
                spotColor = colorResource(id = R.color.gStart)
            )
            .clickable {
                navHostController.navigate(AppScreens.AddEditLeave.route + "/" + leave.id + "/" + true)
            },
        elevation = 3.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            shape = CircleShape, color = colorResource(
                                id = R.color.gStart
                            ).copy(0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.ic_calender),
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.padding(10.dp)) {
                    if (leave.leaveDays == SINGLE_DAY) {


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                LeaveDescription(s = "Date", true)
                                Spacer(modifier = Modifier.height(3.dp))
                                LeaveDate(leave.date)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            VerticalDivider()
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                LeaveDescription(s = "Type", true)
                                Spacer(modifier = Modifier.height(3.dp))
                                LeaveDate(leave.leaveType)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                LeaveDescription(s = "From", true)
                                Spacer(modifier = Modifier.height(3.dp))
                                LeaveDate(leave.startDate)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            VerticalDivider()
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                LeaveDescription(s = "To", true)
                                Spacer(modifier = Modifier.height(3.dp))
                                LeaveDate(leave.endDate)
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LeaveDescription("Details:", true)
                    Spacer(modifier = Modifier.height(3.dp))
                    LeaveDescription(leave.reason)
                }
            }
        }
    }
}

@Composable
fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(30.dp)
            .rotate(90f), color = Color.LightGray
    )
}

@Composable
private fun LeaveDescription(s: String, light: Boolean = false) {
    Text(
        text = s,
        fontSize = 10.sp,
        fontFamily = montserratFontFamily,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black.copy(if (light) 0.3f else 0.6f),
        lineHeight = 15.sp,
        maxLines = 1,
    )
}

@Composable
fun LeaveDate(startDate: String) {
    Text(
        text = startDate,
        fontSize = 14.sp,
        fontFamily = montserratFontFamily,
        fontWeight = FontWeight.SemiBold
    )
}
