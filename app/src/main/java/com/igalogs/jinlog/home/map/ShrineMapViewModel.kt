package com.igalogs.jinlog.home.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.igalogs.jinlog.data.Result
import com.igalogs.jinlog.data.model.Place
import com.igalogs.jinlog.data.place.PlaceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ShrineMapViewModel @ViewModelInject constructor(private var placeRepository: PlaceRepository) :
    ViewModel() {

    private var job: Job? = null

    private val _hitPlaces = MutableLiveData<List<Place>>()
    val hitPlaces: LiveData<List<Place>> = _hitPlaces

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isDataLoadingError = MutableLiveData<Boolean>()
    val isDataLoadingError: LiveData<Boolean> = _isDataLoadingError

    fun onCameraIdel(latLng: LatLng) {
        _dataLoading.value = true
        job?.cancel()
        job = viewModelScope.launch {
            val result = placeRepository.getPlacesByLatLong(latLng.latitude, latLng.longitude)

            if (result is Result.Success) {
                _hitPlaces.value = result.data
                _isDataLoadingError.value = false
            } else {
                _isDataLoadingError.value = true
            }

            _dataLoading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}