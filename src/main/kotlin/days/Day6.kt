package days

import Day
import java.io.File
import kotlin.math.nextDown
import kotlin.math.nextUp
import kotlin.math.sqrt

class Day6 : Day {

    override fun run() {
        val lines = File("src/main/resources/Day6.txt").readText().lines()
        val parsedLines = lines.map { it.split(" ").drop(1).filterNot { it.isEmpty() } }
        val races = parsedLines[0].zip(parsedLines[1]) // Pair (Time, Distance)

        // Part 1
        val winningButtonPushDurationRanges = races.map { determineWinningRange(it) }
        println("Part 1: " + winningButtonPushDurationRanges.map { it.count() }.reduce { acc, i -> acc * i }) // 288

        // Part 2
        val race = lines.map { it.substringAfter(':').filterNot { it.isWhitespace() } }.zipWithNext().first()
        println("Part 2: " + determineWinningRange(race).count()) // 71503
    }

    /**
     * solve quadratic equation and get solution range from x1 to x2
     * (with x being the amount of ms the button was pressed)
     * targetDistance = x * (maxTime - x)
     * x1,2 = (-maxTime +/- (sqrt(maxTime * maxTime - 4 * targetDistance))) / -2
     */
    private fun determineWinningRange(race: Pair<String, String>): LongRange {
        val maxTime = race.first.toDouble()
        val targetDistance = race.second.toDouble()
        val x1 = (-maxTime + (sqrt(maxTime * maxTime - 4 * targetDistance))) / -2
        val x2 = (-maxTime - (sqrt(maxTime * maxTime - 4 * targetDistance))) / -2
        return x1.nextUp().toLong() + 1..x2.nextDown().toLong()
    }
}
