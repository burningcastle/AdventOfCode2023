package days

import Day
import java.io.File

class Day1 : Day {

    override fun run() {
        val lines = File("src/main/resources/Day1.txt").readText().lines()
        val digits = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
        val spelledOutDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        // Part 1
        println("Part 1: " + lines.sumOf { line -> getCalibrationValue(line, digits) }) // 53194

        // Part 2
        println("Part 2: " + lines.sumOf { line -> getCalibrationValue(line, digits + spelledOutDigits) }) // 54249
    }

    private fun getCalibrationValue(line: String, digitStrings: List<String>): Int =
        line.firstOf(digitStrings).toDigit() * 10 + line.lastOf(digitStrings).toDigit()

    private fun String.toDigit(): Int = when (this) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        else -> this.toInt()
    }

    private fun String.firstOf(searchTerms: List<String>): String = this.findAnyOf(searchTerms)!!.second
    private fun String.lastOf(searchTerms: List<String>): String = this.findLastAnyOf(searchTerms)!!.second

}
