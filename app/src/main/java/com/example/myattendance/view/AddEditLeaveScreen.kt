package com.example.myattendance.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myattendance.R
import com.example.myattendance.database.Leave
import com.example.myattendance.utils.CustomTextField
import com.example.myattendance.viewmodel.MainViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by Sajid Ali Suthar.
 */

var date: String = ""
var month: String = ""
var reason: String = ""
var leaveId: String = ""


@Composable
fun AddEditLeaveScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    leaveId: String?,
    isEdit: Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    var validationMessageShown by remember { mutableStateOf(false) }
    var selectedLeaveType by remember { mutableStateOf("Full Day") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("dd MMM yyyy").format(selectedDate)
        }
    }
    var dateEdited by remember { mutableStateOf(false) }

    clearAll()
    if (isEdit) {
        leaveId?.let { it ->
            mainViewModel.getLeaveById(it).observeAsState().value?.let {
                selectedLeaveType = it.leaveType
                date = it.date
                month = it.month
                reason = it.reason
            }
        }
    }else{
        date = formattedDate
        month = formattedDate.split(" ")[1]
    }
    suspend fun showValidationMsg() {
        if (!validationMessageShown) {
            validationMessageShown = true
            delay(3000)
            validationMessageShown = false
        }
    }

    val scrollState = rememberScrollState()
    var isEdited by remember { mutableStateOf(false) }


    val dateDialogState = rememberMaterialDialogState()

    Scaffold(
        content = { padding ->
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
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

                            Text(
                                modifier = Modifier.padding(20.dp),
                                text = if (isEdit) "Edit Leave" else "Add Leave",
                                textAlign = TextAlign.Start,
                                color = Color.White,
                                style = MaterialTheme.typography.h5,
                                fontWeight = FontWeight.Light
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        painter = painterResource(id = R.drawable.holyday),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(300.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "Full Day",
                            modifier = Modifier
                                .background(
                                    shape = RoundedCornerShape(50.dp),
                                    color = if (selectedLeaveType == "Full Day") {
                                        colorResource(
                                            id = R.color.gStart
                                        )
                                    } else {
                                        colorResource(
                                            id = R.color.transparent
                                        )
                                    }
                                )
                                .padding(10.dp)
                                .weight(1f)
                                .clickable {
                                    selectedLeaveType = "Full Day"
                                    isEdited = true
                                },
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            color = if (selectedLeaveType == "Full Day") {
                                Color.White
                            } else {
                                colorResource(id = R.color.primaryColor)
                            }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Half Day",
                            modifier = Modifier
                                .background(
                                    shape = RoundedCornerShape(50.dp),
                                    color = if (selectedLeaveType == "Half Day") {
                                        colorResource(
                                            id = R.color.gStart
                                        )
                                    } else {
                                        colorResource(
                                            id = R.color.transparent
                                        )
                                    }
                                )
                                .padding(10.dp)
                                .weight(1f)
                                .clickable {
                                    selectedLeaveType = "Half Day"
                                    isEdited = true
                                },
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            color = if (selectedLeaveType == "Half Day") {
                                Color.White
                            } else {
                                colorResource(id = R.color.primaryColor)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .fillMaxWidth()
                            .height(55.dp)
                            .border(
                                BorderStroke(1.dp, Color.Gray),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .clickable {
                                dateDialogState.show()
                                isEdited = true
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(15.dp),
                            text = if (isEdit && !dateEdited) "Leave Date: $date" else "Leave Date: $formattedDate",
                            style = MaterialTheme.typography.caption,
                            color = Color.DarkGray
                        )
                    }


                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        inputWrapper = reason,
                        labelResId = R.string.leave_reason,
                        maxLength = 100,
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                    ) {
                        isEdited = true
                        reason = it
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gStart)),
                        onClick = {
                            if (isEdited) {
                                if (isEdit) {
                                    updateLeaveInDB(
                                        Leave(
                                            leaveId?.toInt(),
                                            selectedLeaveType,
                                            date,
                                            month,
                                            reason
                                        ), mainViewModel
                                    )
                                } else {
                                    addLeaveInDB(
                                        Leave(
                                            id = null,
                                            leaveType = selectedLeaveType,
                                            date = date,
                                            month = month,
                                            reason = reason
                                        ), mainViewModel
                                    )
                                }
                                clearAll()
                                navHostController.popBackStack()
                            } else {
                                coroutineScope.launch {
                                    showValidationMsg()
                                }
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = if (isEdit) "UPDATE" else "ADD",
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                }
                MaterialDialog(dialogState = dateDialogState,
                    buttons = {
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel")
                    }) {
                    datepicker(
                        initialDate = if (isEdit && !dateEdited) LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM yyyy")) else selectedDate,
                        title = "Select leave date",
                    ) {
                        dateEdited = true
                        selectedDate = it
                    }
                }

            }
        }
    )
}


fun addLeaveInDB(leave: Leave, mainViewModel: MainViewModel) {
    mainViewModel.addLeave(leave)
}

fun updateLeaveInDB(leave: Leave, mainViewModel: MainViewModel) {
    mainViewModel.updateLeave(leave)
}

fun clearAll() {
    date = ""
    month = ""
    reason = ""
    leaveId = ""
}
