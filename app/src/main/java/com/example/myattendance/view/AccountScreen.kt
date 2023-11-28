package com.example.myattendance.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.myattendance.R
import com.example.myattendance.database.User
import com.example.myattendance.utils.AppScreens
import com.example.myattendance.utils.DAILY_SALARY
import com.example.myattendance.utils.TYPE_NAME
import com.example.myattendance.utils.TYPE_SALARY
import com.example.myattendance.utils.TYPE_YEARLY_BONUS
import com.example.myattendance.utils.USER_NAME
import com.example.myattendance.utils.YEARLY_BONUS
import com.example.myattendance.viewmodel.MainViewModel

/**
 * Created by Sajid Ali Suthar.
 */
@Composable
fun AccountScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel
) {
    mainViewModel.getUser()
    val user = mainViewModel.user.value
    Scaffold{ padding ->
        Column(
            modifier = Modifier
                .background(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                )
                .fillMaxHeight()
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
                            .padding(20.dp).clickable { navHostController.popBackStack() },
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            Color.White
                        )
                    )

                   Text(
                        modifier = Modifier.padding(20.dp),
                        text = "Account",
                        textAlign = TextAlign.Start,
                        color = Color.White,
                        style = MaterialTheme.typography.h5,
                       fontWeight = FontWeight.Light
                    )
                }

            }

            AccountItem(title = TYPE_NAME, user?.name ?: USER_NAME, mainViewModel)
            AccountItem(title = TYPE_SALARY, user?.dailySalary ?: DAILY_SALARY, mainViewModel)
            AccountItem(title = TYPE_YEARLY_BONUS, user?.yearlyBonus ?: YEARLY_BONUS, mainViewModel)
        }
    }
}

@Composable
fun AccountItem(title: String, value: String, mainViewModel: MainViewModel) {
    var updatedValue by remember { mutableStateOf(value) }
    var isDialogVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.clickable { isDialogVisible = true }) {
        Row(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = updatedValue,
                style = MaterialTheme.typography.caption
            )
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
    if (isDialogVisible) {
        EditItemDialog(
            title = {
                Text(
                    text = "Enter $title",
                    style = MaterialTheme.typography.h6,
                )
            },
            content = {
                OutlinedTextField(
                    value = updatedValue,
                    onValueChange = {
                        if (it.length <=6){
                            updatedValue = it
                        }else if (title == TYPE_NAME){
                            updatedValue = it
                        }
                    },
                    label = { Text(title) },
                    keyboardOptions = if (title == TYPE_NAME) {
                        KeyboardOptions(keyboardType = KeyboardType.Text)
                    } else {
                        KeyboardOptions(keyboardType = KeyboardType.Number)
                    }
                )
            },
            dismissButton = {
                Button(
                    onClick = { isDialogVisible = false },
                    content = { Text("Cancel") },
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val user = mainViewModel.user.value
                        when (title) {
                            TYPE_NAME -> user?.name = updatedValue
                            TYPE_SALARY -> user?.dailySalary = updatedValue
                            TYPE_YEARLY_BONUS -> user?.yearlyBonus = updatedValue
                        }
                        updateValueToDB(user, mainViewModel)
                        isDialogVisible = false
                    },
                    content = { Text("Save") },
                )
            },
            onDismiss = {
                isDialogVisible = false
            }
        )
    }
}

fun updateValueToDB(user: User?, mainViewModel: MainViewModel) {
    mainViewModel.updateUser(user)
}

@Composable
fun EditItemDialog(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Column(Modifier.padding(24.dp)) {
                    title.invoke()
                    Spacer(Modifier.size(16.dp))
                    content.invoke()
                }
                Spacer(Modifier.size(4.dp))
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    dismissButton.invoke()
                    confirmButton.invoke()
                }
            }
        }
    }
}
