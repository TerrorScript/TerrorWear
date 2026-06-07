package com.terrsus.terrorwear.features.games.tilt.domain.logic

import com.terrsus.terrorwear.domain.math.Vector2

data class TiltInput(
    val tilt: Vector2,          // from wrist angle, normalized [-1, 1]
    val isJumpPressed: Boolean, // tap gesture
    val deltaTimeSec: Float,    // frame delta
    val nowMillis: Long         // for timers / regen
)
