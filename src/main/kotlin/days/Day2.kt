package days

import Day
import java.io.File

class Day2 : Day {

    override fun run() {
        val lines = File("src/main/resources/Day2.txt").readText().lines()
        val games = lines.map { it.split(", ", ": ", "; ").drop(1).map { it.split(" ") } }

        // Part 1
        // limits: 12 red cubes, 13 green cubes, and 14 blue cubes
        val red = 12
        val green = 13
        val blue = 14
        val possibleGames = games.mapIndexedNotNull { index, game ->
            val isPossibleGame = game.none { revealedCubes ->
                when (revealedCubes.color()) {
                    "red" -> revealedCubes.amount() > red
                    "blue" -> revealedCubes.amount() > blue
                    "green" -> revealedCubes.amount() > green
                    else -> false
                }
            }
            if (isPossibleGame) index + 1 else null
        }
        println("Part 1: " + possibleGames.sum()) // 2061

        // Part 2
        val powers = games.map { game ->
            val redCubes = game.filter { it.color() == "red" }.maxOf { it.amount() }
            val blueCubes = game.filter { it.color() == "blue" }.maxOf { it.amount() }
            val greenCubes = game.filter { it.color() == "green" }.maxOf { it.amount() }
            redCubes * greenCubes * blueCubes
        }
        println("Part 2: " + powers.sum()) // 72596
    }

    private fun List<String>.color() = this.last()
    private fun List<String>.amount() = this.first().toInt()

}
