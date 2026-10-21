package game

enum class GameLevel(val value: Int) {

    EASY(10), MEDIUM(5), HARD(3);

    companion object {
        fun of(input: Int): GameLevel =
            when(input) {
                1 -> EASY
                2 -> MEDIUM
                3 -> HARD
                else -> throw IllegalArgumentException("Unknown game level")
            }
    }
}