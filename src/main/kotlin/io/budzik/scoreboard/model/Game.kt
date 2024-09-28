package io.budzik.scoreboard.model

import java.time.Instant

typealias GameId = Int

data class Game(
    val gameId: GameId,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Short,
    val awayScore: Short,
    val startTime: Instant,
)