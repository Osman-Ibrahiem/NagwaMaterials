package com.osman.materials.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.core.viewstate.BaseVS
import com.domain.core.viewstate.Idle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<I : BaseIntent> : ViewModel() {

    val intentsChannel = Channel<I>(Channel.UNLIMITED)

    private val _states = MutableStateFlow<BaseVS>(Idle)
    val states: StateFlow<BaseVS> get()= _states

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intentsChannel.consumeAsFlow().collect(::processIntents)
        }
    }

    abstract fun processIntents(intent: I)

    fun sendState(state: BaseVS) {
        viewModelScope.launch {
            _states.value = state
        }
    }
}