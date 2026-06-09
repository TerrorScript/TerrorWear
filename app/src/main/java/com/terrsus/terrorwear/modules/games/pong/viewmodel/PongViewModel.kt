package com.terrsus.terrorwear.modules.games.pong.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.features.games.pong.domain.logic.PongAi
import com.terrsus.terrorwear.features.games.pong.domain.logic.PongPhysics
import com.terrsus.terrorwear.features.games.pong.domain.logic.PongRules
import com.terrsus.terrorwear.features.games.pong.domain.model.Collision
import com.terrsus.terrorwear.features.games.pong.domain.model.GameState
import com.terrsus.terrorwear.features.games.pong.domain.model.PongPhase
import com.terrsus.terrorwear.features.games.pong.domain.model.initialGameState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Controls Pong game state, physics, AI, and player input.
 */
class PongViewModel : ViewModel() {

    private val physics = PongPhysics()
    private val ai = PongAi()

    private var initialized = false
    lateinit var rules: PongRules

    private val _state = MutableStateFlow(GameState.Companion.placeholder())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Collision>()
    val event = _event.asSharedFlow()

    private val _phase = MutableStateFlow(PongPhase.Menu)
    val phase = _phase.asStateFlow()

    /**
     * Initializes the game using screen size.
     *
     * @param width Screen width in pixels.
     * @param height Screen height in pixels.
     */
    fun initialize(width: Float, height: Float) {
        if (initialized) return
        initialized = true

        rules = PongRules(width, height)
        _state.value = initialGameState(width, height, rules)
    }

    /**
     * Advances the game simulation.
     *
     * @param deltaTime Delta time in seconds.
     */
    fun step(deltaTime: Float) {
        val current = _state.value

        val movedBall = physics.updateBall(current.ball, deltaTime)
        val movedEnemy = ai.updateEnemy(current, deltaTime, rules)

        var newState = current.copy(
            ball = movedBall,
            enemyPaddle = movedEnemy,
            totalTime = current.totalTime + deltaTime
        )

        val ruleResult = rules.applyRules(newState, deltaTime)
        newState = ruleResult.state
        ruleResult.event?.let { event ->
            viewModelScope.launch {
                _event.emit(event)
            }
        }

        _state.value = newState
    }

    /**
     * Moves the player's paddle vertically.
     *
     * @param deltaY Drag distance in pixels.
     */
    fun onPlayerDrag(deltaY: Float) {
        Log.d("TW/Pong", "Drag: $deltaY")

        val current = _state.value
        val paddle = current.playerPaddle

        val newY = paddle.position.y + deltaY
        val clampedY = newY.coerceIn(
            rules.top,
            rules.bottom - paddle.height
        )

        _state.value = current.copy(
            playerPaddle = paddle.copy(
                position = paddle.position.copy(y = clampedY)
            )
        )
    }

    /**
     * Starts gameplay from the menu.
     */
    fun startGame() {
        _phase.value = PongPhase.Playing
    }

    /**
     * Resumes gameplay from pause.
     */
    fun resume() {
        _phase.value = PongPhase.Playing
    }

    /**
     * Handles back button behavior.
     *
     * @return true if consumed.
     */
    fun onBackPressed(): Boolean {
        return when (phase.value) {
            PongPhase.Playing -> {
                _phase.value = PongPhase.Paused
                true
            }

            PongPhase.Paused -> false
            PongPhase.Menu -> false
        }
    }

    /**
     * Resets the game and returns to menu.
     */
    fun restart() {
        _state.value = initialGameState(
            rules.screenWidth,
            rules.screenHeight,
            rules
        )
        _phase.value = PongPhase.Menu
    }
}