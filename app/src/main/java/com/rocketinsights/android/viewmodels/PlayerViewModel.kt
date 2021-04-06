package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.models.Player
import com.rocketinsights.android.repos.PlayerRepository
import com.rocketinsights.android.viewmodels.event.Event
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

    private val _showErrorMessage = MutableLiveData<Event<Boolean>>()
    val showErrorMessage: LiveData<Event<Boolean>>
        get() = _showErrorMessage

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = _message

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
                _showErrorMessage.value = Event(false)
            } catch (e: Throwable) {
                _isLoading.value = false
                _showErrorMessage.value = Event(true)
                Timber.e(e, ERROR_GET_PLAYER)
            }
        }
    }

    fun showMessage(message: String) {
        _message.value = Event(message)
    }
}
