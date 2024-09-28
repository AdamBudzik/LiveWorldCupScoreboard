package io.budzik.scoreboard.model

sealed class ScoreboardError(msg: String? = null) : RuntimeException(msg)

class GameAlreadyExistsError(
    firstTeam: String,
    secondTeam: String,
    val existingGame: Game,
) : ScoreboardError(
    "Either [$firstTeam] or [$secondTeam] already plays a game: [$existingGame]."
)

class GameNotFoundError(gameId: GameId) : ScoreboardError(
    "The game with ID [$gameId] does not exist."
)