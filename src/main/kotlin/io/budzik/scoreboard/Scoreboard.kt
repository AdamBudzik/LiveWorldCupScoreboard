package io.budzik.scoreboard

import io.budzik.scoreboard.model.Game
import io.budzik.scoreboard.model.GameAlreadyExistsError
import io.budzik.scoreboard.model.GameId
import io.budzik.scoreboard.model.GameNotFoundError
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class Scoreboard {
    private val games: ConcurrentHashMap<GameId, Game> = ConcurrentHashMap()
    private val gameIdCounter: AtomicInteger = AtomicInteger(0)
    private val log = LoggerFactory.getLogger(Scoreboard::class.java)

    fun startGame(homeTeam: String, awayTeam: String): Game {
        findGameWithAnyOfTheTeams(homeTeam, awayTeam)?.let { existingGame ->
            throw GameAlreadyExistsError(homeTeam, awayTeam, existingGame).also { log.error(it.message) }
        }
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
        val game = games[gameId] ?: throw GameNotFoundError(gameId).also { log.error(it.message) }
        if (homeScore < 0 || awayScore < 0) {
            throw IllegalArgumentException("All scores should be non-negative.").also { log.error(it.message) }
        }
        val newGame = game.copy(homeScore = homeScore, awayScore = awayScore)
        games[gameId] = newGame
        log.debug("Updated a game with ID [$gameId]. New state: [$newGame].")
        return newGame
    }

    fun finishGame(gameId: GameId) {
        if (!games.containsKey(gameId)) {
            throw GameNotFoundError(gameId).also { log.error(it.message) }
        }
        games.remove(gameId)
        log.debug("Finished game with ID [$gameId].")
    }

    private val gameComparator = compareByDescending<Game> { it.homeScore + it.awayScore }
        .thenByDescending { it.startTime }

    fun getSummary(): List<Game> = games.values.sortedWith(gameComparator)

    private fun findGameWithAnyOfTheTeams(homeTeam: String, awayTeam: String): Game? {
        val searchedTeams = listOf(homeTeam, awayTeam)
        return games.values.find {
            listOf(it.homeTeam, it.awayTeam).any { playingTeam ->
                searchedTeams.any { seachedTeam -> seachedTeam.equals(playingTeam, ignoreCase = true) }
            }
        }
    }
}