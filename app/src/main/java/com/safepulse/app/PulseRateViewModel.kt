package com.safepulse.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PulseRateViewModel : ViewModel() {
    private val pulseRateThreshold = 100 // Define your threshold
    private val monitorDuration = 30 * 1000L // 30 seconds
    private var isHigh = false

    private val _pulseRate = MutableStateFlow("Pulse Rate: -- bpm")
    val pulseRate: StateFlow<String> = _pulseRate

    var onEmergencyAlert: (() -> Unit)? = null

    fun updatePulseRate(newRate: Int) {
        _pulseRate.value = "Pulse Rate: $newRate bpm"
        if (newRate > pulseRateThreshold) {
            if (!isHigh) {
                isHigh = true
                monitorHighRate()
            }
        } else {
            isHigh = false
        }
    }

    private fun monitorHighRate() {
        viewModelScope.launch {
            delay(monitorDuration)
            if (isHigh) {
                // Trigger emergency alert
                onEmergencyAlert?.invoke()
            }
        }
    }
}
