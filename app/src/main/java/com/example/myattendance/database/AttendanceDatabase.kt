package com.example.myattendance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Sajid Ali Suthar.
 */
@Database(entities = [User::class, Leave::class, Advance::class], version = 1)
abstract class AttendanceDatabase: RoomDatabase() {
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: AttendanceDatabase? = null

        fun getDatabase(context: Context): AttendanceDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AttendanceDatabase::class.java,
                        "Attendance_DB"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}