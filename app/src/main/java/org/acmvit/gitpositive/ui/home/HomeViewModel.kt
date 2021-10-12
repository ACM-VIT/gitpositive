package org.acmvit.gitpositive.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.acmvit.gitpositive.local.UserDao
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {

    private val _userList: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val userList: LiveData<List<String>> = _userList

    fun getFilterData(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val flowList = userDao.getAllUser(searchQuery)

            flowList.collect { list -> _userList.postValue(list.map { it.username }.take(3)) }
        }
    }
}