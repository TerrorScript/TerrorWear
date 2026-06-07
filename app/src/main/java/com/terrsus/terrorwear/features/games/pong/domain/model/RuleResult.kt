package com.terrsus.terrorwear.features.games.pong.domain.model

data class RuleResult(
    val state: GameState,
    val event: Collision?
)