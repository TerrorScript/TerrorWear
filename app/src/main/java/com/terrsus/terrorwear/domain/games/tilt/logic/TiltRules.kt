package com.terrsus.terrorwear.domain.games.tilt.logic

import com.terrsus.terrorwear.domain.games.tilt.model.*
import com.terrsus.terrorwear.domain.math.Vector2
import com.terrsus.terrorwear.features.sensors.OrientationData
import kotlin.math.hypot

/**
 * Game rules for Tilt.
 *
 * Handles:
 * - ball movement
 * - obstacles
 * - collisions
 * - jump immunity
 * - life loss and regen
 */
class TiltRules(
    private val playfieldRadius: Float,
    private val maxLives: Int = 3,
    private val lifeRegenMillis: Long = 10_000L,
    private val obstacleWarningMillis: Long = 800L,
    private val obstacleSpeed: Float = 220f,
    private val accelScale: Float = 400f,
    private val obstacleExpireRadius: Float = 20f
) {

    /** Default tilt when the game starts. */
    val initialTilt: Vector2 = Vector2(0f, 0f)

    fun initialState(nowMillis: Long): GameState =
        initialGameState(nowMillis)

    /**
     * Converts raw orientation data into a normalized tilt vector.
     *
     * @param orientation Raw pitch/roll/yaw from sensors.
     * @return Tilt vector in range [-1, 1].
     */
    fun mapOrientationToTilt(orientation: OrientationData): Vector2 {
        val x = (orientation.roll / 45f).coerceIn(-1f, 1f)
        val y = (orientation.pitch / 45f).coerceIn(-1f, 1f)
        return Vector2(x, y)
    }

    /**
     * Main update step for the Tilt game.
     */
    fun update(state: GameState, input: TiltInput): GameState {
        var s = state

        s = updateBall(s, input)
        s = checkBallOutOfBounds(s, input.nowMillis)
        s = updateObstacles(s, input)
        s = checkObstacleCollisions(s, input.nowMillis)
        s = checkWallCollisions(s)
        s = checkPowerupPickup(s)

        if (input.isJumpPressed) {
            s = onJump(s, input.nowMillis)
        }

        s = maybeRegenLife(s, input.nowMillis)

        return s
    }

    // -------------------------------------------------------------------------
    // Everything below remains unchanged
    // -------------------------------------------------------------------------

    private fun updateBall(state: GameState, input: TiltInput): GameState {
        val accel = input.tilt * accelScale
        val vel = state.ball.velocity + accel * input.deltaTimeSec
        val pos = state.ball.position + vel * input.deltaTimeSec

        return state.copy(
            ball = state.ball.copy(
                position = pos,
                velocity = vel
            )
        )
    }

    private fun checkBallOutOfBounds(state: GameState, now: Long): GameState {
        val p = state.ball.position
        val dist = hypot(p.x, p.y)

        if (dist > playfieldRadius) {
            return onLifeLost(state, now)
        }
        return state
    }

    private fun onLifeLost(state: GameState, now: Long): GameState {
        val newLives = (state.lives - 1).coerceAtLeast(0)
        return state.copy(
            lives = newLives,
            lastLifeLostAt = now
        )
    }

    private fun maybeRegenLife(state: GameState, now: Long): GameState {
        if (state.lives >= maxLives) return state
        val last = state.lastLifeLostAt ?: return state
        if (now - last < lifeRegenMillis) return state

        return state.copy(
            lives = (state.lives + 1).coerceAtMost(maxLives),
            lastLifeLostAt = now
        )
    }

    private fun onJump(state: GameState, now: Long): GameState {
        return state.copy(
            jumpUntilMillis = now + 300L
        )
    }

    private fun isImmune(state: GameState, now: Long): Boolean {
        val until = state.jumpUntilMillis ?: return false
        return now <= until
    }

    private fun updateObstacles(state: GameState, input: TiltInput): GameState {
        val now = input.nowMillis
        val dt = input.deltaTimeSec

        val shouldSpawn = (now / 1500L) != (state.lastObstacleSpawnedAtMillis / 1500L)

        val list = state.obstacles.toMutableList()

        if (shouldSpawn) {
            list += spawnObstacleAtBorder(now)
        }

        val updated = list.map { o ->
            when (o.phase) {
                ObstaclePhase.WARNING_BORDER -> {
                    if (now - o.spawnedAtMillis >= obstacleWarningMillis) {
                        o.copy(phase = ObstaclePhase.ACTIVE_IN_FIELD)
                    } else o
                }
                ObstaclePhase.ACTIVE_IN_FIELD -> {
                    val newPos = o.position + o.velocity * dt
                    o.copy(position = newPos)
                }
                ObstaclePhase.EXPIRED -> o
            }
        }.filterNot { it.phase == ObstaclePhase.EXPIRED }

        return state.copy(
            obstacles = updated,
            lastObstacleSpawnedAtMillis = if (shouldSpawn) now else state.lastObstacleSpawnedAtMillis
        )
    }

    private fun spawnObstacleAtBorder(now: Long): Obstacle {
        val side = (0..3).random()

        val pos = when (side) {
            0 -> Vector2(-playfieldRadius, 0f)
            1 -> Vector2(playfieldRadius, 0f)
            2 -> Vector2(0f, -playfieldRadius)
            else -> Vector2(0f, playfieldRadius)
        }

        val dir = Vector2(-pos.x, -pos.y)
        val len = hypot(dir.x, dir.y)
        val norm = Vector2(dir.x / len, dir.y / len)

        return Obstacle(
            id = now,
            position = pos,
            velocity = norm * obstacleSpeed,
            spawnedAtMillis = now,
            phase = ObstaclePhase.WARNING_BORDER
        )
    }

    private fun checkObstacleCollisions(state: GameState, now: Long): GameState {
        if (isImmune(state, now)) return state

        val ballPos = state.ball.position

        val hit = state.obstacles.any { o ->
            if (o.phase != ObstaclePhase.ACTIVE_IN_FIELD) return@any false
            val d = hypot(o.position.x - ballPos.x, o.position.y - ballPos.y)
            d < obstacleExpireRadius
        }

        return if (hit) onLifeLost(state, now) else state
    }

    private fun checkWallCollisions(state: GameState): GameState {
        return state
    }

    private fun checkPowerupPickup(state: GameState): GameState {
        val ballPos = state.ball.position

        val (picked, remaining) = state.powerups.partition { p ->
            val d = hypot(p.position.x - ballPos.x, p.position.y - ballPos.y)
            d < 20f
        }

        if (picked.isEmpty()) return state

        var s = state.copy(powerups = remaining)

        picked.forEach { p ->
            s = applyPowerup(s, p)
        }

        return s
    }

    private fun applyPowerup(state: GameState, powerup: Powerup): GameState {
        return when (powerup.type) {
            PowerupType.ExtraLife -> {
                val newLives = (state.lives + 1).coerceAtMost(maxLives)
                state.copy(lives = newLives)
            }
            PowerupType.Shield -> {
                state.copy(jumpUntilMillis = (state.jumpUntilMillis ?: 0L) + 1500L)
            }
            PowerupType.SlowMotion -> state
        }
    }
}