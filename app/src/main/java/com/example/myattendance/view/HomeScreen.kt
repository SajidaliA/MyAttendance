package com.example.myattendance.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myattendance.R
import com.example.myattendance.database.Advance
import com.example.myattendance.database.Leave
import com.example.myattendance.utils.AppScreens
import com.example.myattendance.utils.NAVIGATE_ADVANCE
import com.example.myattendance.utils.NAVIGATE_LEAVE
import com.example.myattendance.utils.TYPE_ADVANCE
import com.example.myattendance.utils.TYPE_BONUS
import com.example.myattendance.utils.TYPE_DAY
import com.example.myattendance.utils.TYPE_LEAVE
import com.example.myattendance.utils.TYPE_MONTH
import com.example.myattendance.utils.TYPE_YEAR
import com.example.myattendance.utils.TYPE_YEAR_WITH_BONUS
import com.example.myattendance.utils.USER_NAME
import com.example.myattendance.utils.montserratFontFamily
import com.example.myattendance.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by Sajid Ali Suthar.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel
) {

    val c = Calendar.getInstance()
    val monthNum = SimpleDateFormat("MMMM", Locale.ENGLISH)
    val yearNum = SimpleDateFormat("yyyy", Locale.ENGLISH)
    val currentMonth = monthNum.format(c.time)
    var selectedMonth by rememberSaveable { mutableStateOf(currentMonth) }
    val currentYear = yearNum.format(c.time)

    mainViewModel.getUser()
    mainViewModel.getLeaveByMonth(selectedMonth.take(3))
    mainViewModel.getAllLeaves()
    mainViewModel.getAllAdvance()

    val lazyRowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )
    var dailySalary = 0f
    var monthlySalary = 0f
    var yearlySalary = 0f
    var bonus = 0f
    var yearlySalaryWithBonus = 0f

    val months = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
        currentYear
    )

    Scaffold(
        content = { padding ->
            val user = mainViewModel.user.observeAsState().value
            user?.let {
                if (user.dailySalary.isNotEmpty()) {
                    dailySalary = user.dailySalary.toFloat()
                    monthlySalary = user.dailySalary.toFloat().times(30)
                    yearlySalary = user.dailySalary.toFloat().times(365)
                    bonus = user.yearlyBonus.toFloat()
                    yearlySalaryWithBonus = user.dailySalary.toFloat().times(365) + bonus
                }

            }

            val leaveByMonth: List<Leave> by mainViewModel.leavesByMonth.observeAsState(initial = listOf())
            val leaves: List<Leave> by mainViewModel.allLeaves.observeAsState(initial = listOf())
            val advanceList: List<Advance> by mainViewModel.advanceList.observeAsState(initial = listOf())
            var totalMonthLeaveAmount: Float? = 0f
            var totalMonthAdvanceAmount = 0f
            var totalYearLeaveAmount: Float? = 0f
            var totalYearAdvanceAmount = 0f

            Column(
                modifier = Modifier
                    .background(
                        color = colorResource(id = R.color.background)
                    )
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {

                var currentMonthLeaves = 0f
                for(leave in leaveByMonth){
                    currentMonthLeaves += leave.leaveDayCount.toFloat()
                }

                var yearlyLeaves = 0f
                for(leave in leaves){
                    yearlyLeaves += leave.leaveDayCount.toFloat()
                }
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            modifier = Modifier
                                .rotate(180f)
                                .size(70.dp)
                                .padding(horizontal = 20.dp),
                            painter = painterResource(id = R.drawable.ic_drawer),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                Color.White
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Image(modifier = Modifier
                            .clickable {
                                navHostController.navigate(AppScreens.Account.route)
                            }
                            .size(100.dp)
                            .padding(horizontal = 20.dp),
                            painter = painterResource(id = R.drawable.ic_user_avatar),
                            contentDescription = null)
                    }


                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = "Hello! ${user?.name ?: USER_NAME}",
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        color = Color.White.copy(0.8f)
                    )

                    if (leaveByMonth.isNotEmpty()) {
                        totalMonthLeaveAmount = user?.dailySalary?.toFloat()?.let {
                            (currentMonthLeaves).times(
                                it
                            )
                        }
                    }
                    if (leaves.isNotEmpty()) {
                        totalYearLeaveAmount = user?.dailySalary?.toFloat()?.let {
                            (yearlyLeaves).times(it)
                        }
                    }

                    mainViewModel.getAdvanceByMonth(selectedMonth.take(3))
                    val advanceByMonth =
                        mainViewModel.advanceListByMonth.observeAsState(initial = listOf())
                    if (advanceByMonth.value.isNotEmpty()) {
                        for (i in advanceByMonth.value) {
                            totalMonthAdvanceAmount += i.amount.toFloat()
                        }
                    }
                    if (advanceList.isNotEmpty()) {
                        for (i in advanceList) {
                            totalYearAdvanceAmount += i.amount.toFloat()
                        }
                    }
                    val finalMonthlySalary =
                        monthlySalary - totalMonthLeaveAmount!! - totalMonthAdvanceAmount

                    val finalYearlySalary =
                        yearlySalary - totalYearLeaveAmount!! - totalYearAdvanceAmount

                    val finalMonthlySalaryWithComma = "%,d".format(finalMonthlySalary.toInt())
                    val finalYearlySalaryWithComma = "%,d".format(finalYearlySalary.toInt())

                    Text(
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 20.dp,
                        ),
                        text = if (selectedMonth == currentYear) {
                            "₹${finalYearlySalaryWithComma}/-"
                        } else {
                            "₹${finalMonthlySalaryWithComma}/-"
                        },
                        textAlign = TextAlign.Start,
                        color = Color.White,
                        fontSize = 30.sp,
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 20.dp
                        ),
                        text = selectedMonth,
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        color = Color.White.copy(0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
                LaunchedEffect(key1 = Unit) {
                    lazyRowState.animateScrollToItem(months.indexOf(currentMonth))
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    state = lazyRowState,
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(items = months) { monthItem ->
                        Text(
                            fontFamily = montserratFontFamily,
                            fontWeight = FontWeight.Light,
                            text = monthItem,
                            modifier = Modifier
                                .background(
                                    shape = RoundedCornerShape(50.dp),
                                    color = if (selectedMonth == monthItem) {
                                        colorResource(
                                            id = R.color.gEnd
                                        )
                                    } else {
                                        colorResource(
                                            id = R.color.transparent
                                        )
                                    }
                                )
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                                .clickable {
                                    selectedMonth = monthItem
                                    if (selectedMonth == currentYear) {
                                        mainViewModel.getAllLeaves()
                                    } else {
                                        mainViewModel.getLeaveByMonth(monthItem.take(3))
                                    }

                                },
                            textAlign = TextAlign.Center,
                            color = if (selectedMonth == monthItem) {
                                Color.White
                            } else {
                                colorResource(id = R.color.primaryColor)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 80.dp)
                    ) {
                        CardItem(
                            TYPE_DAY, dailySalary, navHostController = navHostController
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        CardItem(
                            TYPE_YEAR, yearlySalary, navHostController = navHostController
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        CardItem(
                            TYPE_YEAR_WITH_BONUS,
                            yearlySalaryWithBonus,
                            navHostController = navHostController
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        CardItem(
                            TYPE_ADVANCE,
                            if (selectedMonth == currentYear) {
                                totalYearAdvanceAmount
                            } else {
                                totalMonthAdvanceAmount
                            },
                            selectedMonth = selectedMonth,
                            navHostController = navHostController
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 80.dp)
                    ) {
                        CardItem(
                            TYPE_MONTH, monthlySalary, navHostController = navHostController
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        CardItem(
                            TYPE_BONUS, bonus, navHostController = navHostController
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        CardItem(
                            type = TYPE_LEAVE,
                            amount = if (selectedMonth == currentYear) {
                                totalYearLeaveAmount
                            } else {
                                totalMonthLeaveAmount
                            },
                            currentMonthLeaves = if (selectedMonth == currentYear) {
                                yearlyLeaves
                            } else {
                                currentMonthLeaves
                            },
                            selectedMonth = selectedMonth,
                            navHostController = navHostController
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    if (modalSheetState.isVisible)
                        modalSheetState.hide()
                    else
                        modalSheetState.show()
                }
            }, backgroundColor = colorResource(id = R.color.gEnd)) {
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        }
    )
    BottomSheetLayout(navHostController, modalSheetState, coroutineScope)
}

@Composable
private fun CardItem(
    type: String,
    amount: Float?,
    currentMonthLeaves: Float = 0f,
    selectedMonth: String = "",
    navHostController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                10.dp,
                RoundedCornerShape(20.dp),
                spotColor = colorResource(id = R.color.gStart)
            )
            .height(100.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    if (type == TYPE_LEAVE) {
                        navHostController.navigate(AppScreens.LeaveList.routeWithArgs(selectedMonth))
                    } else if (type == TYPE_ADVANCE) {
                        navHostController.navigate(
                            AppScreens.AdvanceList.routeWithArgs(
                                selectedMonth
                            )
                        )
                    }
                },
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(start = 20.dp, bottom = 2.dp),
                text = when (type) {
                    TYPE_DAY -> "Daily"
                    TYPE_MONTH -> "Monthly"
                    TYPE_YEAR -> "Yearly"
                    TYPE_BONUS -> "Yearly bonus"
                    TYPE_YEAR_WITH_BONUS -> "Yearly + bonus"
                    TYPE_LEAVE -> "$currentMonthLeaves Leave amount"
                    TYPE_ADVANCE -> "Advance"
                    else -> {
                        ""
                    }
                },
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = colorResource(id = R.color.gEnd).copy(0.6f)
            )
            val finalAmountWithComma = "%,d".format(amount?.toInt())
            Text(
                modifier = Modifier.padding(start = 20.dp, top = 2.dp, bottom = 20.dp),
                text = "₹${finalAmountWithComma}/-",
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = if (type == TYPE_LEAVE || type == TYPE_ADVANCE) {
                    Color.Red
                } else if (type == TYPE_BONUS) {
                    colorResource(id = R.color.secondaryLightColor)
                } else {
                    colorResource(id = R.color.primaryColor)
                }
            )
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetLayout(
    navHostController: NavHostController,
    modalSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {


    val isSheetFullscreen by remember { mutableStateOf(false) }
    val roundedCornerRadius = if (isSheetFullscreen) 0.dp else 20.dp
    val modifier = if (isSheetFullscreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = roundedCornerRadius,
            topEnd = roundedCornerRadius
        ),
        sheetContent = {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                BottomSheetRow(
                    painterResource(id = R.drawable.ic_calender),
                    NAVIGATE_LEAVE,
                    navHostController,
                    modalSheetState,
                    coroutineScope
                )
                BottomSheetRow(
                    painterResource(id = R.drawable.ic_money),
                    NAVIGATE_ADVANCE, navHostController,
                    modalSheetState,
                    coroutineScope
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    ) {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetRow(
    painter: Painter,
    navigateType: String,
    navHostController: NavHostController,
    modalSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {

    Card(
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp, top = 30.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(25),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    when (navigateType) {
                        NAVIGATE_LEAVE -> {
                            navHostController.navigate(AppScreens.AddEditLeave.route + "/" + "0" + "/" + false)
                        }

                        NAVIGATE_ADVANCE -> {
                            navHostController.navigate(AppScreens.AddEditAdvance.route + "/" + "0" + "/" + false)
                        }
                    }
                    coroutineScope.launch {
                        modalSheetState.hide()
                    }
                }
                .background(color = colorResource(id = R.color.gStart).copy(0.1f)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(vertical = 20.dp)
            )
            Text(
                text = navigateType,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
            )
        }
    }


}