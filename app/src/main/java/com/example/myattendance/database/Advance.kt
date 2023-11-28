package com.example.myattendance.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Sajid Ali Suthar.
 */
@Entity
data class Advance(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var amount: String,
    var date: String,
    var month: String,
    var details: String
)
