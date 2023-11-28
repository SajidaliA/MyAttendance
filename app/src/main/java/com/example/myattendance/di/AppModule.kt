package com.example.myattendance.di

import com.example.myattendance.repository.MainRepository
import com.example.myattendance.database.AttendanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Sajid Ali Suthar.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMainRepository(attendanceDao: AttendanceDao) : MainRepository {
        return MainRepository(attendanceDao)
    }
}