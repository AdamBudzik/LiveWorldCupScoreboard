package io.budzik.scoreboard

import io.budzik.scoreboard.model.Game
import io.budzik.scoreboard.model.GameId
import org.slf4j.LoggerFactory

class Scoreboard {
    private val log = LoggerFactory.getLogger(Scoreboard::class.java)

    fun startGame(homeTeam: String, awayTeam: String): Game {
        TODO()
    }

    fun updateGame(gameId: GameId, homeScore: Short, awayScore: Short): Game {
        TODO()
    }

    fun finishGame(gameId: GameId) {
        TODO()
    }

    fun getSummary(): List<Game> {
        TODO()
    }
}