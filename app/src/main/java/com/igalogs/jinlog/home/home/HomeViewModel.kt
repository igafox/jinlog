package com.igalogs.jinlog.home.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.log.LogRepository
import com.igalogs.jinlog.data.model.Log
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(private var logRepository: LogRepository) : ViewModel() {

    val logs = MutableLiveData<List<Log>?>()

    fun get() {

        viewModelScope.launch {
            val result = logRepository.getLogs(20, null)
            if (result is Result.Success) {
                logs.value = result.data
            } else {
                logs.value = null
            }

        }

    }

}