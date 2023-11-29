package com.example.myattendance.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myattendance.R
import com.example.myattendance.database.Advance
import com.example.myattendance.utils.AppScreens
import com.example.myattendance.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by Sajid Ali Suthar.
 */

@Composable
fun AdvanceListScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    selectedMonth: String?,
) {

    val c = Calendar.getInstance()
    val yearNum = SimpleDateFormat("yyyy", Locale.ENGLISH)
    val currentYear = yearNum.format(c.time)
    if (selectedMonth == currentYear) {
        mainViewModel.getAllAdvance()
    } else {
        mainViewModel.getAdvanceByMonth(selectedMonth?.take(3).toString())
    }

    val advanceListByMonth: List<Advance> by mainViewModel.advanceListByMonth.observeAsState(initial = listOf())
    val advanceList: List<Advance> by mainViewModel.advanceList.observeAsState(initial = listOf())
    var advancesToDisplay: List<Advance>
    val lazyListState = rememberLazyListState()

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.background))
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
                            text = "$selectedMonth Advance",
                            textAlign = TextAlign.Start,
                            color = Color.White,
                            style = androidx.compose.material.MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Light
                        )
                    }

                }

                advancesToDisplay = if (selectedMonth == currentYear) {
                    advanceList
                } else {
                    advanceListByMonth
                }
                if (advancesToDisplay.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 20.dp),
                        state = lazyListState
                    ) {
                        items(items = advancesToDisplay) { advance ->
                            AdvanceCard(advance, navHostController)
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
                            text = "No any Advance taken.",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    )

}


@Composable
fun AdvanceCard(advance: Advance, navHostController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        backgroundColor = Color.White,
        elevation = 2.dp
    ) {
        Column(modifier = Modifier
            .padding(20.dp)
            .clickable {
                navHostController.navigate(
                    AppScreens.AddEditAdvance.route + "/" + advance.id + "/" + true
                )
            }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.advance_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(50.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "₹${advance.amount}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W300
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = advance.details, fontSize = 12.sp, fontWeight = FontWeight.Light)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = advance.date,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}