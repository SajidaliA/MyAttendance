package com.example.myattendance.database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sajid Ali Suthar.
 */
@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var id: Int = 0,
    var name: String = "",
    var dailySalary: String = "",
    var yearlyBonus: String = ""
)
