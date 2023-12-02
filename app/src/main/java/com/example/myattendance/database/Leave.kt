package com.example.myattendance.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sajid Ali Suthar.
 */
@Entity
data class Leave(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    var leaveDays: String = "",
    var leaveType: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var date: String = "",
    var leaveDayCount: String = "",
    var month: String = "",
    var reason: String = ""
)
