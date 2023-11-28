package com.example.myattendance.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myattendance.R
import com.example.myattendance.utils.AppScreens

/**
 * Created by Sajid Ali Suthar.
 */

private val screens = listOf(
    AppScreens.HomeScreen,
    AppScreens.Account
)

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) {
        Column(
            modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_account),
                contentDescription = "App icon",
                colorFilter = ColorFilter.tint(colorResource(id = R.color.secondaryColor)),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(50.dp))
            )
        }
        screens.forEach { screen ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onDestinationClicked(screen.route) })
                    .height(45.dp)
                    .padding(start = 10.dp)
            ) {
                Icon(painterResource(id = screen.icon), screen.title, tint = colorResource(id = R.color.secondaryColor))
                Spacer(modifier = Modifier.width(7.dp))
                Text(text = screen.title, style = MaterialTheme.typography.body2, modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth())
            }
            Divider(color = Color.LightGray)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Developed by Sajid Ali Suthar",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally))
    }
}
