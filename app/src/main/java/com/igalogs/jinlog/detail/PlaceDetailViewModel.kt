package com.igalogs.jinlog.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.log.LogRepository
import com.igalogs.jinlog.data.model.Log
import com.igalogs.jinlog.data.model.Place
import com.igalogs.jinlog.data.place.PlaceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlaceDetailViewModel @ViewModelInject constructor(
    private var placeRepository: PlaceRepository,
    private var logRepository: LogRepository
) : ViewModel() {

    private var job: Job? = null

    private val placeId: String? = null

    private val _place = MutableLiveData<Place?>()
    val place: LiveData<Place?> = _place

    private val _log = MutableLiveData<List<Log>?>()
    val log: LiveData<List<Log>?> = _log

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isDataLoadingError = MutableLiveData<Boolean>()
    val isDataLoadingError: LiveData<Boolean> = _isDataLoadingError

    fun load(placeId: String) {
        if (_dataLoading.value == true || placeId == this.placeId) {
            return
        }
        viewModelScope.launch {
            _dataLoading.value = true

            val place = placeRepository.getPlace(placeId)
            val log = logRepository.getLogsByPlaceId(placeId)

            if (place is Result.Success && log is Result.Success) {
                _isDataLoadingError.value = false
                _place.value = place.data
                _log.value = log.data
            } else {
                _isDataLoadingError.value = true
                _place.value = null
                _log.value = null
            }

            _dataLoading.value = false
        }
    }

}