package io.budzik.scoreboard

import io.budzik.scoreboard.model.Game
import io.budzik.scoreboard.model.GameId
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class Scoreboard {
    private val games: ConcurrentHashMap<GameId, Game> = ConcurrentHashMap()
    private val gameIdCounter: AtomicInteger = AtomicInteger(0)
    private val log = LoggerFactory.getLogger(Scoreboard::class.java)

    fun startGame(homeTeam: String, awayTeam: String): Game {
        val id = gameIdCounter.incrementAndGet()
        val game = Game(
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeScore = 0,
            awayScore = 0,
            startTime = Instant.now(),
            gameId = id
        )
        games[id] = game
        log.debug("Created a game with ID [$id]: [$game]")
        return game
    }

    fun updateGame(gameId: GameId, homeScore: Short, awayScore: Short): Game {
        TODO()
    }

    fun finishGame(gameId: GameId) {
        TODO()
    }

    fun getSummary(): List<Game> = games.values.toList()
}