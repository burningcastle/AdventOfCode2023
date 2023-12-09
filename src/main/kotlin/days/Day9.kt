package days

import Day
import java.io.File

class Day9 : Day {

    override fun run() {
        val lines = File("src/main/resources/Day9.txt").readText().lines()
        val values = lines.map { line -> line.split(" ").map { it.toLong() } }

        // Part 1
        println("Part 1: " + values.sumOf { nextValue(it) }) // 114 // 1772145754

        // Part 2
        println("Part 2: " + values.map { it.reversed() }.sumOf { nextValue(it) }) // 2 // 867
    }

    private fun nextValue(valueHistory: List<Long>): Long {
        val nextSequence = valueHistory.zipWithNext { a, b -> b - a }
        return if (nextSequence.all { it == 0L }) {
            valueHistory.last()
        } else {
            valueHistory.last() + nextValue(nextSequence)
        }
    }
}
