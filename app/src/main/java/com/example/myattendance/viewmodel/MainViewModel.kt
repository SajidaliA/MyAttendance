package com.example.myattendance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myattendance.database.Advance
import com.example.myattendance.database.Leave
import com.example.myattendance.database.User
import com.example.myattendance.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Sajid Ali Suthar.
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    var user: LiveData<User> = mainRepository.user
    var allLeaves: LiveData<List<Leave>> = mainRepository.allLeaves
    var leavesByMonth: LiveData<List<Leave>> = mainRepository.leavesByMonth
    var advanceList: LiveData<List<Advance>> = mainRepository.allAdvance
    var advanceListByMonth: LiveData<List<Advance>> = mainRepository.advanceByMonth

    fun updateUser(user: User?){
        if (user != null) {
            mainRepository.addUser(user)
        }
    }

    fun getUser() {
        mainRepository.getUser()
    }

    fun addLeave(leave: Leave) {
        mainRepository.addLeave(leave)
        getAllLeaves()
    }

    fun deleteLeave(leave: Leave) {
        mainRepository.deleteLeave(leave)
    }

    fun getAllLeaves() {
        mainRepository.getAllLeaves()
    }

    fun getLeaveByMonth(month: String?) {
       mainRepository.getLeaveByMonth(month)
    }

    fun getLeaveById(id: String): LiveData<Leave> {
        return mainRepository.getLeaveById(id)
    }

    fun addAdvance(advance: Advance) {
        mainRepository.addAdvance(advance)
        getAllAdvance()
    }

    fun deleteAdvance(advance: Advance, month: String?) {
        mainRepository.deleteAdvance(advance)
        getAllAdvance()
        getAdvanceByMonth(month)
    }

    fun getAllAdvance() {
        mainRepository.getAllAdvance()
    }

    fun getAdvanceByMonth(month: String?) {
        mainRepository.getAdvanceByMonth(month)
    }

    fun getAdvanceById(id: String): LiveData<Advance> {
        return mainRepository.getAdvanceById(id)
    }

    fun updateLeave(leave: Leave) {
        mainRepository.updateLEave(leave)
        getAllLeaves()
    }

    fun updateAdvance(advance: Advance) {
        mainRepository.updateAdvance(advance)
        getAllAdvance()
    }
}