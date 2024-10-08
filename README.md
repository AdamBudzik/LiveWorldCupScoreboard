# Scoreboard Library

The scoreboard supports the following operations:

1. Start a new game, assuming initial score 0 – 0 and adding it the scoreboard. This should capture following
   parameters:
   a. Home team
   b. Away team
2. Update score. This should receive a pair of absolute scores: home team score and away team score.
3. Finish game currently in progress. This removes a match from the scoreboard.
4. Get a summary of games in progress ordered by their total score. The games with the same
   total score will be returned ordered by the most recently started match in the scoreboard.

## Model

### Game

**Fields:**

- `gameId: GameId` - Unique ID of Game within the scoreboard.
- `homeTeam: String` - Name of the home team.
- `awayTeam: String` - Name of the away team.
- `homeScore: Short` - Score of the home team.
- `awayScore: Short` - Score of the away team.
- `startTime: Instant` - The start date of game in UTC format.

## API

### startGame(homeTeam: String, awayTeam: String): Game

Starts a new game for a given a pair of home and away team.
The initial game score is 0-0.

#### Returns

`Game` definition

#### Throws

- `GameAlreadyExistsError` - for cases when trying to start a game with a team that already plays another game.

### updateGame(gameId: GameId, homeScore: Short, awayScore: Short): Game

Updates existing game with a new score. Requires an identifier of existing game and non-negative score values.

#### Returns

`Game` - updated game.

#### Throws

- `GameNotFoundError` - when trying to update non-existing game.
- `IllegalArgumentException` - when trying to set a score to a negative score.

### finishGame(gameId: GameId)

Finishes the game that is currently in progress. Removes it from the scoreboard. Throws error when trying to finish a
game that does not exist.

#### Returns

``Unit``

#### Throws

- `GameNotFoundError` - when trying to finish non-existing game.

### getSummary()

Returns a summary of games in progress ordered by their total score. The games with the same
total score will be returned ordered by the most recently started match in the scoreboard.

#### Returns

``List<Game>`` - ordered list of games in progress.

#### Throws

- Does not throw errors

## Sample usage

```kotlin
val scoreboard = Scoreboard()
// Start a new game
val game = scoreboard.startGame(homeTeam = "Mexico", awayTeam = "Germany")
// use game ID to update the score
scoreboard.updateGame(game.gameId, 1, 2)
// start another game
val secondGame = scoreboard.startGame(homeTeam = "Poland", awayTeam = "Romania")
// prints games in progress
// [Game(gameId=2, homeTeam=Poland, awayTeam=Romania, homeScore=0, awayScore=0, startTime=2024-09-28T08:46:21.305939Z), Game(gameId=1, homeTeam=Mexico, awayTeam=Germany, homeScore=0, awayScore=0, startTime=2024-09-28T08:46:21.285854Z)]
val games = scoreboard.getSummary()
// finish existing game
scoreboard.finishGame(game.gameId)
```

More examples in [ScoreboardTest.kt](src/test/kotlin/io/budzik/scoreboard/ScoreboardTest.kt).

## Project Build

To build the project, use the Gradle `build` task. You can execute it via the Gradle wrapper with the following command:
`./gradlew build`
This task will compile the project, run all tests and generate the following output:

- The library JAR,
- The sources JAR.

### Technical Requirements

- Java 17

## Assumptions

- This library is specifically designed for managing live scores in Football World Cup matches. It does not support
  other sports.
- A team can participate in only one game at any given time.
- The implementation is intentionally simple. The following features were omitted:
- Additional interfaces (e.g., for the Scoreboard),
- Extra types (e.g., for Team, Score),
- Complex validation of existing games or scores.
- The library is written in Kotlin, a language I've been working with extensively for the past few years and feel most
  comfortable with.
