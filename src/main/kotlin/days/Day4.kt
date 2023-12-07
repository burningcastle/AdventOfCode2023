package days

import Day
import java.io.File
import kotlin.math.pow

// TODO needs refactoring!
class Day4 : Day {

    override fun run() {
        val lines = File("src/main/resources/Day4.example.txt").readText().lines()
        val scratchCards = lines.map { line ->
            line.split(": ", " | ").drop(1)
                .map { numberString -> numberString.split(" ").filter { it.isNotEmpty() } }
        }
        val amountOfWinningNumbers = scratchCards
            .map { card ->
                card[0].filter { number -> number in card[1] }
            }.map { winningNumbers -> winningNumbers.size }

        // Part 1
        println("Part 1: " + amountOfWinningNumbers.filter { it > 0 }.sumOf { 2.0.pow((it - 1).toDouble()) }) // 13

        // Part 2
        val scratchCardsWithAmounts = amountOfWinningNumbers.map { it to 1 }.toMutableList()
        scratchCardsWithAmounts.forEachIndexed { index, pair ->
            val indexRange = index + 1..minOf(index + pair.first, scratchCardsWithAmounts.lastIndex)
            for (i in indexRange) {
                scratchCardsWithAmounts[i] =
                    scratchCardsWithAmounts[i].copy(second = scratchCardsWithAmounts[i].second + pair.second)
            }
        }
        println("Part 2: " + scratchCardsWithAmounts.sumOf { it.second }) // 30
    }

}
