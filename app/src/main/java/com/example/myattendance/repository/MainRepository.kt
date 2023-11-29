package com.example.myattendance.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myattendance.database.Advance
import com.example.myattendance.database.AttendanceDao
import com.example.myattendance.database.Leave
import com.example.myattendance.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Sajid Ali Suthar.
 */
class MainRepository(private val attendanceDao: AttendanceDao) {

    val user = MutableLiveData<User>()
    val allLeaves = MutableLiveData<List<Leave>>()
    val leavesByMonth = MutableLiveData<List<Leave>>()
    val allAdvance = MutableLiveData<List<Advance>>()
    val advanceByMonth = MutableLiveData<List<Advance>>()
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addUser(user: User){
        coroutineScope.launch(Dispatchers.IO) {
            attendanceDao.addUser(user)
        }
    }

    fun addLeave(leave: Leave){
        coroutineScope.launch(Dispatchers.IO) {
            attendanceDao.addLeave(leave)
        }
    }

    fun addAdvance(advance: Advance){
        coroutineScope.launch(Dispatchers.IO) {
            attendanceDao.addAdvance(advance)
        }
    }

    fun getUser() {
        coroutineScope.launch(Dispatchers.IO) {
            if (attendanceDao.getUser().isNotEmpty()){
                user.postValue(attendanceDao.getUser()[0])
            }else{
                user.postValue(User())
            }
        }
    }

    fun getAllLeaves() {
        coroutineScope.launch(Dispatchers.IO) {
            allLeaves.postValue(attendanceDao.getAllLeaves())
        }
    }

    fun getLeaveById(id: String): LiveData<Leave>  {
        return attendanceDao.getLeaveById(id)
    }
    fun getAdvanceById(id: String): LiveData<Advance>  {
        return attendanceDao.getAdvanceById(id)
    }

    fun getLeaveByMonth(month: String?)  {
        coroutineScope.launch(Dispatchers.IO) {
            leavesByMonth.postValue(attendanceDao.getLeaveByMonth(month))
        }
    }

    fun getAllAdvance()  {
        coroutineScope.launch(Dispatchers.IO) {
            allAdvance.postValue(attendanceDao.getAllAdvance())
        }
    }

    fun getAdvanceByMonth(month: String?) {
        coroutineScope.launch(Dispatchers.IO) {
            advanceByMonth.postValue(attendanceDao.getAdvanceByMonth(month))
        }
    }

    fun deleteLeave(leave: Leave){
        coroutineScope.launch(Dispatchers.IO) {
            attendanceDao.deleteLeave(leave)
        }
    }

    fun deleteAdvance(advance: Advance){
        coroutineScope.launch(Dispatchers.IO) {
            attendanceDao.deleteAdvance(advance)
        }
    }

    fun updateLEave(leave: Leave) {
        coroutineScope.launch(Dispatchers.IO) {
            attendanceDao.updateLeave(leave)
        }
    }

    fun updateAdvance(advance: Advance) {
        coroutineScope.launch(Dispatchers.IO) {
            attendanceDao.updateAdvance(advance)
        }
    }
}