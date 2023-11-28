package com.example.myattendance.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sajid Ali Suthar.
 */
@Entity
data class Leave(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var leaveType: String,
    var date: String,
    var month: String,
    var reason: String
)
