package com.terrsus.terrorwear.domain.games.pong.model

import com.terrsus.terrorwear.domain.games.pong.model.GameState
import com.terrsus.terrorwear.domain.games.pong.model.Collision

data class RuleResult(
    val state: GameState,
    val event: Collision?
)