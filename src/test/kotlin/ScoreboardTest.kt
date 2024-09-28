import io.budzik.scoreboard.Scoreboard
import io.budzik.scoreboard.model.GameAlreadyExistsError
import io.budzik.scoreboard.model.GameNotFoundError
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class ScoreboardTest : ShouldSpec({

    context("Score board") {
        should("start a new game with initial 0-0 score") {
            val scoreboard = Scoreboard()
            scoreboard.getSummary().size shouldBe 0
        }

        should("return empty scoreboard if no there are no active games") {
            val scoreboard = Scoreboard()
            scoreboard.getSummary().size shouldBe 0
        }

        should("update scores") {
            val scoreboard = Scoreboard()
            val game = scoreboard.startGame(homeTeam = "Mexico", awayTeam = "Germany")

            scoreboard.updateGame(game.gameId, 1, 2)

            val games = scoreboard.getSummary()
            games.size shouldBe 1
            games.first() should { result ->
                result.homeTeam shouldBe "Mexico"
                result.awayTeam shouldBe "Germany"
                result.homeScore shouldBe 1
                result.awayScore shouldBe 2
                result.startTime shouldBe result.startTime
            }
        }


        should("throw error when trying to start a game with a team that already plays") {
            val scoreboard = Scoreboard()
            val existingGame = scoreboard.startGame(homeTeam = "Mexico", awayTeam = "Germany")
            val error = shouldThrow<GameAlreadyExistsError> {
                scoreboard.startGame(awayTeam = "Germany", homeTeam = "Mexico")
            }
            error.existingGame shouldBe existingGame
        }

        should("not allow negative scores while updating game score") {
            val scoreboard = Scoreboard()
            val game = scoreboard.startGame(homeTeam = "Mexico", awayTeam = "Germany")
            assertThrows<IllegalArgumentException> {
                scoreboard.updateGame(game.gameId, -1, 0)
            }
        }

        should("throw error when trying to update non existing game") {
            val scoreboard = Scoreboard()
            shouldThrow<GameNotFoundError> {
                scoreboard.updateGame(1, 1, 2)
            }
        }

        should("return games summary ordered by score and the start time") {
            val scoreboard = Scoreboard()
            val lowestScore = scoreboard.startGame(homeTeam = "Mexico", awayTeam = "Germany")
            val biggestScoreOlder = scoreboard.startGame(homeTeam = "Poland", awayTeam = "Czech Republic")
            val biggestScoreNewest = scoreboard.startGame(homeTeam = "Slovakia", awayTeam = "UK")

            scoreboard.updateGame(lowestScore.gameId, 1, 0)
            scoreboard.updateGame(biggestScoreOlder.gameId, 2, 1)
            scoreboard.updateGame(biggestScoreNewest.gameId, 0, 3)

            val games = scoreboard.getSummary()
            games.size shouldBe 3
            games.map { it.gameId } shouldBeEqual listOf(
                biggestScoreNewest.gameId,
                biggestScoreOlder.gameId,
                lowestScore.gameId
            )
        }

        should("remove finished game") {
            val scoreboard = Scoreboard()
            val game = scoreboard.startGame(homeTeam = "Mexico", awayTeam = "Germany")
            scoreboard.finishGame(game.gameId)
            scoreboard.getSummary().size shouldBe 0
        }

        should("throw error when trying to finish non existing game") {
            val scoreboard = Scoreboard()
            shouldThrow<GameNotFoundError> {
                scoreboard.finishGame(1)
            }
        }
    }
})