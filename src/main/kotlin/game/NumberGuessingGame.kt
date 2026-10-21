package game

import kotlin.random.Random
import kotlin.random.nextInt

class NumberGuessingGame(private val guessingNumber: Int = Random.nextInt(1..100)) {

    private var gameLevel: GameLevel? = null
    private var guessesLeft: Int = 0

    private val greetingMessage = """
        Welcome to the Number Guessing Game!
        I'm thinking of a number between 1 and 100.
        You have 5 chances to guess the correct number.
        
    """.trimIndent()

    private val levelChoseMessage = """
        Please select the difficulty level:
        1. Easy (10 chances)
        2. Medium (5 chances)
        3. Hard (3 chances)
        
        Enter your choice:
    """.trimIndent()

    fun play() = System.`in`.bufferedReader().use { reader ->
        println(greetingMessage)

        generateSequence(GameStatus.INIT) { gameStatus ->
            when (gameStatus) {
                GameStatus.INIT -> initGame {
                    print(levelChoseMessage)

                    val gameLevel = reader
                        .readLine()
                        .trim()
                        .toInt()
                        .let(GameLevel::of)

                    println(
                        """
                            Great! You have selected the ${gameLevel.name} difficulty level.
                            Let's start the game!
                            
                        """.trimIndent()
                    )

                    gameLevel
                }

                GameStatus.IN_PROGRESS -> step {
                    print("Enter your guess: ")

                    val guess = reader
                        .readLine()
                        .trim()
                        .toInt()
                    guessesLeft--

                    when {
                        guess == guessingNumber -> {
                            println("Congratulations! You guessed the correct number in ${(gameLevel?.value ?: 0) - guessesLeft} attempts.")
                            GameStatus.END
                        }
                        guessesLeft <= 0 -> {
                            println("Sorry... There are no guesses left.")
                            GameStatus.END
                        }
                        else -> {
                            println("Incorrect! The number is ${if (guessingNumber < guess) "less" else "greater"} than $guess.")
                            GameStatus.IN_PROGRESS
                        }
                    }
                }

                GameStatus.END -> {
                    print("Do you want to play one more time? [y/n]: ")

                    reader
                        .readLine()
                        .trim()
                        .let { input ->
                            when (input.lowercase()) {
                                "y", "yes" -> GameStatus.INIT
                                "n", "no" -> GameStatus.TERMINATE
                                else -> GameStatus.END
                            }
                        }
                }

                GameStatus.TERMINATE -> GameStatus.TERMINATE
            }
        }.takeWhile { it != GameStatus.TERMINATE }
            .forEach {  }
    }

    fun initGame(gameLevelInit: () -> GameLevel): GameStatus {
        gameLevel = gameLevelInit()
        guessesLeft = gameLevel?.value ?: 0

        return GameStatus.IN_PROGRESS
    }

    fun step(gameLevelStep: () -> GameStatus): GameStatus = gameLevelStep()
}
