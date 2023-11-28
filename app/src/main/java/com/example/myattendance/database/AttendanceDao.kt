package com.example.myattendance.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Created by Sajid Ali Suthar.
 */
@Dao
interface AttendanceDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLeave(leave: Leave)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAdvance(advance: Advance)

    @Query("select * from user limit 1")
    fun getUser() : List<User>

    @Query("select * from leave")
    fun getAllLeaves() : List<Leave>

    @Query("select * from leave where month =:month")
    fun getLeaveByMonth(month: String) : List<Leave>

    @Query("select * from leave where id =:id")
    fun getLeaveById(id: String) : Leave

    @Query("select * from advance")
    fun getAllAdvance() : List<Advance>

    @Query("select * from advance where month =:month")
    fun getAdvanceByMonth(month: String) : List<Advance>

    @Query("select * from advance where id =:id")
    fun getAdvanceById(id: String) : Advance

    @Delete
    suspend fun deleteLeave(leave: Leave)

    @Delete
    suspend fun deleteAdvance(advance: Advance)

    @Update
    fun updateLeave(leave: Leave)

    @Update
    fun updateAdvance(advance: Advance)
}