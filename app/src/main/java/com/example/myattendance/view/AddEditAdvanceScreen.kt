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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
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
import com.example.myattendance.database.Advance
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


var dateOfTaken: String = ""
var amount: String = ""
var monthOfAdvance: String = ""
var details: String = ""
var advanceId: String = ""

@Composable
fun AddEditAdvanceScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    advanceId: String?,
    isEdit: Boolean
) {
    lateinit var selectedAdvance: Advance
    val coroutineScope = rememberCoroutineScope()
    var validationMessageShown by remember { mutableStateOf(false) }
    clearAdvance()
    if (isEdit) {
        advanceId?.let { mainViewModel.getAdvanceById(it) }
        selectedAdvance = mainViewModel.advanceById.observeAsState().value!!
        dateOfTaken = selectedAdvance.date
        amount = selectedAdvance.amount
        monthOfAdvance = selectedAdvance.month
        details = selectedAdvance.details
    }
    suspend fun showValidationMsg() {
        if (!validationMessageShown) {
            validationMessageShown = true
            delay(3000)
            validationMessageShown = false
        }
    }


    var isEdited by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("dd MMM yyyy").format(selectedDate)
        }
    }
    val dateDialogState = rememberMaterialDialogState()

    Scaffold(
        content = { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
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
                    ){

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
                                text = if (isEdit) "Edit Advance" else "Add Advance",
                                textAlign = TextAlign.Start,
                                color = Color.White,
                                style = androidx.compose.material.MaterialTheme.typography.h5,
                                fontWeight = FontWeight.Light
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        painter = painterResource(id = R.drawable.money),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(350.dp)
                    )
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
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(15.dp),
                            text = "Leave Date: $formattedDate",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        inputWrapper = amount,
                        labelResId = R.string.advance_amount,
                        maxLength = 7,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                    ) {
                        isEdited = true
                        amount = it
                    }
                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        inputWrapper = details,
                        labelResId = R.string.details,
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
                        details = it
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.gStart)),
                        onClick = {
                            dateOfTaken = formattedDate
                            monthOfAdvance = formattedDate.split(" ")[1]
                            if (isEdited) {
                                if (isEdit) {
                                    updateAdvanceInDB(Advance(advanceId?.toInt(), amount, dateOfTaken, monthOfAdvance, details), mainViewModel)
                                } else {
                                    addAdvanceInDB(Advance(null, amount, dateOfTaken, monthOfAdvance, details), mainViewModel)
                                }
                                clearAdvance()
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
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Light,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
                MaterialDialog (dialogState = dateDialogState,
                    buttons = {
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel")
                    }) {
                    datepicker(
                        initialDate = LocalDate.now(),
                        title = "Select advance date",
                    ){
                        selectedDate = it
                    }
                }
        }
    )
}

fun addAdvanceInDB(advance: Advance, mainViewModel: MainViewModel) {
    mainViewModel.addAdvance(advance)
}

fun updateAdvanceInDB(advance: Advance, mainViewModel: MainViewModel) {
    mainViewModel.updateAdvance(advance)
}

fun clearAdvance() {
    dateOfTaken = ""
    amount = ""
    monthOfAdvance = ""
    details = ""
    advanceId = ""
}
