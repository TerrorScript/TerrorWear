package com.terrsus.terrorwear.modules.games.tilt.viewmodel

import androidx.lifecycle.ViewModel
import com.terrsus.terrorwear.features.games.tilt.domain.logic.TiltInput
import com.terrsus.terrorwear.features.games.tilt.domain.logic.TiltRules
import com.terrsus.terrorwear.features.games.tilt.domain.model.GameState
import com.terrsus.terrorwear.features.games.tilt.domain.model.TiltPhase
import com.terrsus.terrorwear.features.sensors.OrientationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Pure Tilt game ViewModel.
 *
 * - No Android dependencies
 * - No sensors
 * - No frame clock
 * - Screen pushes tilt/tap + deltaTime
 */
class TiltViewModel : ViewModel() {

    private val rules = TiltRules(playfieldRadius = 200f)

    private val _state = MutableStateFlow(rules.initialState(System.currentTimeMillis()))
    val state: StateFlow<GameState> = _state

    private val _phase = MutableStateFlow(TiltPhase.Menu)
    val phase: StateFlow<TiltPhase> = _phase

    // Latest input from screen
    private var latestTilt = rules.initialTilt
    private var tapPending = false

    /**
     * Called by the screen when new orientation data arrives.
     */
    fun onTilt(orientation: OrientationData) {
        latestTilt = rules.mapOrientationToTilt(orientation)
    }

    /**
     * Called by the screen when a tap gesture is detected.
     */
    fun onTap() {
        tapPending = true
    }

    /**
     * Called by the screen every frame.
     *
     * @param deltaTimeSec Time since last frame.
     * @param nowMillis Current timestamp.
     */
    fun step(deltaTimeSec: Float, nowMillis: Long) {
        if (phase.value != TiltPhase.Playing) return

        val input = TiltInput(
            tilt = latestTilt,
            isJumpPressed = tapPending,
            deltaTimeSec = deltaTimeSec,
            nowMillis = nowMillis
        )

        tapPending = false

        _state.value = rules.update(_state.value, input)
    }

    /**
     * Starts gameplay from the menu.
     */
    fun startGame() {
        _state.value = rules.initialState(System.currentTimeMillis())
        _phase.value = TiltPhase.Playing
    }

    /**
     * Pauses gameplay.
     */
    fun pause() {
        _phase.value = TiltPhase.Paused
    }

    /**
     * Resumes gameplay.
     */
    fun resume() {
        _phase.value = TiltPhase.Playing
    }

    /**
     * Handles back button behavior.
     *
     * @return true if consumed.
     */
    fun onBackPressed(): Boolean {
        return when (phase.value) {
            TiltPhase.Playing -> {
                pause()
                true
            }
            TiltPhase.Paused,
            TiltPhase.Menu -> false
        }
    }
}