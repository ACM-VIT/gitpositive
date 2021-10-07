package org.acmvit.gitpositive.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import org.acmvit.gitpositive.local.UserDao
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {

    val userList =
        userDao.getAllUser().map { userFlow -> userFlow.map { it.username } }.asLiveData()
}