package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.models.Player
import com.rocketinsights.android.repos.PlayerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

private const val ERROR_GET_PLAYER = "Error while retrieving player."

class PlayerViewModel(
    private val repo: PlayerRepository
) : ViewModel() {

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player>
        get() = _player

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError

    init {
        getPlayer()
    }

    fun getPlayer() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                delay(2000) // just to simulate 2 sec delay - remove from production code
                _player.value = repo.getPlayer()
                _isLoading.value = false
                _isError.value = false
            } catch (e: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Timber.e(e, ERROR_GET_PLAYER)
            }
        }
    }
}
