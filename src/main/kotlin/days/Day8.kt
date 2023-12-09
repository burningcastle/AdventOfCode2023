package days

import Day
import java.io.File

class Day8 : Day {

    override fun run() {
        val lines = File("src/main/resources/Day8.txt").readText().lines()
        val instructions = generateSequence(lines.first()) { it.drop(1) + it.first() }.map { it.first() }
        val nodes = lines.drop(2)
            .map { line -> line.split(" = ").map { it.split(", ").map { it.filter { it != '(' && it != ')' } } } }
            .associate { it[0][0] to (it[1][0] to it[1][1]) }

        // Part 1
        println("Part 1: " + calculateSteps(instructions, nodes, "AAA", "ZZZ")) // 6 // 16897

        // Part 2
        val stepCounts = nodes.keys.filter { it.last() == 'A' }.map { calculateSteps(instructions, nodes, it, "Z") }
        println("Part 2: " + findLCM(stepCounts)) // 6 // 16563603485021
    }

    private fun calculateSteps(
        instructions: Sequence<Char>,
        nodes: Map<String, Pair<String, String>>,
        start: String,
        endsWith: String
    ): Long {
        var count = 0L
        var currentPosition = start
        instructions.forEach {
            if (currentPosition.endsWith(endsWith)) return count
            if (it == 'L') {
                currentPosition = nodes[currentPosition]!!.first
            } else {
                currentPosition = nodes[currentPosition]!!.second
            }
            count++
        }
        error("Not found")
    }

    // determine least common multiple (LCM) aka kleinstes gemeinsames Vielfaches (kgV)
    private fun findLCM(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    private fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

}
