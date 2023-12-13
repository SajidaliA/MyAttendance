package com.example.myattendance.view

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.myattendance.utils.ADD_LEAVE
import com.example.myattendance.utils.CustomTextField
import com.example.myattendance.utils.DATE_FORMAT
import com.example.myattendance.utils.EDIT_LEAVE
import com.example.myattendance.utils.END_DATE
import com.example.myattendance.utils.FULL_DAY
import com.example.myattendance.utils.HALF_DAY
import com.example.myattendance.utils.LEAVE_DATE
import com.example.myattendance.utils.MULTIPLE_DAYS
import com.example.myattendance.utils.SINGLE_DAY
import com.example.myattendance.utils.START_DATE
import com.example.myattendance.viewmodel.MainViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Created by Sajid Ali Suthar.
 */

var date: String = ""
var startDate: String = ""
var endDate: String = ""
var month: String = ""
var reason: String = ""
var leaveId: String = ""
var leaveDayCount: String = ""


@Composable
fun AddEditLeaveScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    leaveId: String?,
    isEdit: Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    var validationMessageShown by remember { mutableStateOf(false) }
    var selectedLeaveType by remember { mutableStateOf(FULL_DAY) }
    var selectedLeaveDays by remember { mutableStateOf(SINGLE_DAY) }
    var selectedLeaveDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedStartDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedEndDate by remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    val formattedLeaveDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern(DATE_FORMAT).format(selectedLeaveDate)
        }
    }
    val formattedStartDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern(DATE_FORMAT).format(selectedStartDate)
        }
    }
    val formattedEndDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern(DATE_FORMAT).format(selectedEndDate)
        }
    }
    var leaveDateEdited by remember { mutableStateOf(false) }
    var startDateEdited by remember { mutableStateOf(false) }
    var endDateEdited by remember { mutableStateOf(false) }

    clearAll()
    if (isEdit) {
        leaveId?.let { it ->
            mainViewModel.getLeaveById(it).observeAsState().value?.let {
                selectedLeaveType = it.leaveType
                selectedLeaveDays = it.leaveDays
                date = it.date
                startDate = it.startDate
                endDate = it.endDate
                month = it.month
                reason = it.reason
                leaveDayCount = it.leaveDayCount
            }
        }
    } else {
        date = formattedLeaveDate
        startDate = formattedStartDate
        endDate = formattedEndDate
        month = formattedLeaveDate.split(" ")[1]
    }
    if (leaveDateEdited){
        date = formattedLeaveDate
        month = formattedLeaveDate.split(" ")[1]
    }
    if(startDateEdited){
        startDate = formattedStartDate
        month = formattedStartDate.split(" ")[1]
    }
    if (endDateEdited){
        endDate = formattedEndDate
        month = formattedEndDate.split(" ")[1]
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


    val leaveDateDialogState = rememberMaterialDialogState()
    val startDateDialogState = rememberMaterialDialogState()
    val endDateDialogState = rememberMaterialDialogState()

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
                                text = if (isEdit) EDIT_LEAVE else ADD_LEAVE,
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
                            text = SINGLE_DAY,
                            modifier = Modifier
                                .background(
                                    shape = RoundedCornerShape(50.dp),
                                    color = if (selectedLeaveDays == SINGLE_DAY) {
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
                                    selectedLeaveDays = SINGLE_DAY
                                    isEdited = true
                                },
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            color = if (selectedLeaveDays == SINGLE_DAY) {
                                Color.White
                            } else {
                                colorResource(id = R.color.primaryColor)
                            }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = MULTIPLE_DAYS,
                            modifier = Modifier
                                .background(
                                    shape = RoundedCornerShape(50.dp),
                                    color = if (selectedLeaveDays == MULTIPLE_DAYS) {
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
                                    selectedLeaveDays = MULTIPLE_DAYS
                                    isEdited = true
                                },
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            color = if (selectedLeaveDays == MULTIPLE_DAYS) {
                                Color.White
                            } else {
                                colorResource(id = R.color.primaryColor)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    AnimatedVisibility(visible = selectedLeaveDays == SINGLE_DAY) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = FULL_DAY,
                                    modifier = Modifier
                                        .background(
                                            shape = RoundedCornerShape(50.dp),
                                            color = if (selectedLeaveType == FULL_DAY) {
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
                                            selectedLeaveType = FULL_DAY
                                            isEdited = true
                                        },
                                    fontWeight = FontWeight.Light,
                                    textAlign = TextAlign.Center,
                                    color = if (selectedLeaveType == FULL_DAY) {
                                        Color.White
                                    } else {
                                        colorResource(id = R.color.primaryColor)
                                    }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = HALF_DAY,
                                    modifier = Modifier
                                        .background(
                                            shape = RoundedCornerShape(50.dp),
                                            color = if (selectedLeaveType == HALF_DAY) {
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
                                            selectedLeaveType = HALF_DAY
                                            isEdited = true
                                        },
                                    fontWeight = FontWeight.Light,
                                    textAlign = TextAlign.Center,
                                    color = if (selectedLeaveType == HALF_DAY) {
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
                                        leaveDateDialogState.show()
                                        isEdited = true
                                    },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(15.dp),
                                    text = if (isEdit && !leaveDateEdited) "$LEAVE_DATE: $date" else "$LEAVE_DATE: $formattedLeaveDate",
                                    style = MaterialTheme.typography.caption,
                                    color = Color.DarkGray
                                )
                            }
                        }

                    }
                    AnimatedVisibility(visible = selectedLeaveDays == MULTIPLE_DAYS) {
                        Column {
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
                                        startDateDialogState.show()
                                        isEdited = true
                                    },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(15.dp),
                                    text = if (isEdit && !startDateEdited) "$START_DATE: $startDate" else "$START_DATE: $formattedStartDate",
                                    style = MaterialTheme.typography.caption,
                                    color = Color.DarkGray
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
                                        endDateDialogState.show()
                                        isEdited = true
                                    },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(15.dp),
                                    text = if (isEdit && !endDateEdited) "$END_DATE: $endDate" else "$END_DATE: $formattedEndDate",
                                    style = MaterialTheme.typography.caption,
                                    color = Color.DarkGray
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
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
                                if (selectedLeaveDays == SINGLE_DAY){
                                    startDate = ""
                                    endDate = ""
                                    leaveDayCount = if (selectedLeaveType == FULL_DAY) "1" else "0.5"
                                }else{
                                    selectedLeaveType = ""
                                    date = ""
                                    month = formattedStartDate.split(" ")[1]
                                    leaveDayCount = ChronoUnit.DAYS.between(selectedStartDate, selectedEndDate.plusDays(1)).toString()
                                }
                                if (isEdit) {
                                    updateLeaveInDB(
                                        Leave(
                                            leaveId?.toInt(),
                                            selectedLeaveDays,
                                            selectedLeaveType,
                                            startDate,
                                            endDate,
                                            date,
                                            leaveDayCount,
                                            month,
                                            reason
                                        ), mainViewModel
                                    )
                                } else {
                                    addLeaveInDB(
                                        Leave(
                                            null,
                                            selectedLeaveDays,
                                            selectedLeaveType,
                                            startDate,
                                            endDate,
                                            date,
                                            leaveDayCount,
                                            month,
                                            reason
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
                MaterialDialog(dialogState = leaveDateDialogState,
                    buttons = {
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel")
                    }) {
                    datepicker(
                        initialDate = if (isEdit && !leaveDateEdited) LocalDate.parse(
                            date,
                            DateTimeFormatter.ofPattern("dd MMM yyyy")
                        ) else selectedLeaveDate,
                        title = "Select leave date",
                    ) {
                        leaveDateEdited = true
                        selectedLeaveDate = it
                    }
                }
                val context = LocalContext.current
                MaterialDialog(dialogState = startDateDialogState,
                    buttons = {
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel")
                    }) {
                    datepicker(
                        initialDate = if (isEdit && !startDateEdited) LocalDate.parse(
                            startDate,
                            DateTimeFormatter.ofPattern("dd MMM yyyy")
                        ) else selectedStartDate,
                        title = "Select start date",
                    ) {
                        startDateEdited = true
                        selectedStartDate = it
                        selectedEndDate = selectedStartDate.plusDays(1)
                    }
                }
                MaterialDialog(dialogState = endDateDialogState,
                    buttons = {
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel")
                    }) {
                    datepicker(
                        initialDate = if (isEdit && !endDateEdited) LocalDate.parse(
                            endDate,
                            DateTimeFormatter.ofPattern("dd MMM yyyy")
                        ) else selectedEndDate,

                        title = "Select end date",
                    ) {

                        if (selectedEndDate <= selectedStartDate) {
                            Toast.makeText(
                                context,
                                "End date should be garter then start date",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            endDateEdited = true
                            selectedEndDate = it
                        }
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
