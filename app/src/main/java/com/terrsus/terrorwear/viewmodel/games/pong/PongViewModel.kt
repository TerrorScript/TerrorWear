package com.terrsus.terrorwear.viewmodel.games.pong

import android.util.Log
import androidx.lifecycle.ViewModel
import com.terrsus.terrorwear.domain.games.pong.logic.PongAi
import com.terrsus.terrorwear.domain.games.pong.logic.PongPhysics
import com.terrsus.terrorwear.domain.games.pong.logic.PongRules
import com.terrsus.terrorwear.domain.games.pong.model.initialGameState
import com.terrsus.terrorwear.domain.games.pong.model.GameState
import com.terrsus.terrorwear.domain.games.pong.model.PongPhase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Controls Pong game state, physics, AI, and player input.
 */
class PongViewModel : ViewModel() {

    private val physics = PongPhysics()
    private val ai = PongAi()

    private var initialized = false
    private lateinit var rules: PongRules

    private val _state = MutableStateFlow(GameState.placeholder())
    val state = _state.asStateFlow()

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
     * @param dt Delta time in seconds.
     */
    fun step(dt: Float) {
        val current = _state.value

        val movedBall = physics.updateBall(current.ball, dt)
        val movedEnemy = ai.updateEnemy(current, dt, rules)

        var newState = current.copy(
            ball = movedBall,
            enemyPaddle = movedEnemy,
            totalTime = current.totalTime + dt
        )

        newState = rules.applyRules(newState, dt)
        _state.value = newState
    }

    /**
     * Moves the player's paddle vertically.
     *
     * @param deltaY Drag distance in pixels.
     */
    fun onPlayerDrag(deltaY: Float) {
        Log.d("PONG", "Drag: $deltaY")

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