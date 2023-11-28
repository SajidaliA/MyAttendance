package com.example.myattendance.di

import android.content.Context
import androidx.room.Room
import com.example.myattendance.database.AttendanceDao
import com.example.myattendance.database.AttendanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Sajid Ali Suthar.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAttendanceDao(attendanceDatabase: AttendanceDatabase): AttendanceDao {
        return attendanceDatabase.attendanceDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AttendanceDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AttendanceDatabase::class.java,
            "Attendance_DB"
        ).build()
    }
}