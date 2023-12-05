package days

import Day
import java.io.File

// TODO needs refactoring!
class Day5 : Day {

    override fun run() {
        val paragraphs = File("src/main/resources/Day5.txt").readText().split("\r\n\r\n", "\n\n", "\r\r")
        val seeds = paragraphs.first().split(" ").drop(1).map { it.toLong() }
        val mappings = paragraphs.drop(1)
            .map { it.lines().drop(1) }
            .map { mapping ->
                mapping.map { line ->
                    val row = line.split(" ").map { it.toLong() }
                    val differenceToBeShifted = row[0] - row[1]
                    val range = row[1] until row[1] + row[2]
                    Pair(differenceToBeShifted, range)
                }
            }

        // Part 1
        val locations = seeds.map { seed -> mappings.fold(seed) { number, ranges -> mapNumber(number, ranges) } }
        println("Part 1: " + locations.min()) // 35

        // Part 2
        val seedRanges = seeds.windowed(2, 2).map { it[0] until it[0] + it[1] }
        val locationRanges = applyMappings(seedRanges, mappings)
        println("Part 2: " + locationRanges.minOf { locationRange -> locationRange.first }) // 46
    }

    private fun applyMappings(seedRanges: List<LongRange>, mappings: List<List<Pair<Long, LongRange>>>) =
        seedRanges.flatMap { seedRange ->
            mappings.fold(listOf(seedRange)) { ranges, mappingRanges ->
                ranges.flatMap { mapRange(it, mappingRanges) }
            }
        }

    /**
     * Apply mapping on given range and return resulting ranges (i.e. ranges are split if overlapping)
     */
    private fun mapRange(range: LongRange, mappingRanges: List<Pair<Long, LongRange>>): List<LongRange> {
        val resultingRanges = mutableListOf<LongRange>()
        var currentSeed = range.first
        val lastSeed = range.last
        while (currentSeed <= lastSeed) {
            val rangeContainingSeed = mappingRanges.find { it.range().contains(currentSeed) }
            if (rangeContainingSeed != null) {
                if (rangeContainingSeed.range().last <= lastSeed) {
                    // range doesn't cover all seeds
                    resultingRanges.add(
                        currentSeed + rangeContainingSeed.shift()..rangeContainingSeed.range().last + rangeContainingSeed.shift()
                    )
                    currentSeed = rangeContainingSeed.range().last + 1
                } else {
                    // range covers all seeds
                    resultingRanges.add(
                        currentSeed + rangeContainingSeed.shift()..lastSeed + rangeContainingSeed.shift()
                    )
                    currentSeed = lastSeed + 1
                }
            } else {
                // current seed not in any mapping ranges
                // start a new range for values that won't change
                // find next range to determine how long the newly created range has to be
                val nextRange =
                    mappingRanges.sortedBy { it.range().first }.dropWhile { it.range().first < currentSeed }
                        .firstOrNull()
                if (nextRange == null || nextRange.range().first > lastSeed) {
                    // no further range OR next range is not overlapping
                    resultingRanges.add(currentSeed..lastSeed)
                    currentSeed = lastSeed + 1
                } else {
                    // close range before next range und continue
                    resultingRanges.add(currentSeed until nextRange.range().first)
                    currentSeed = nextRange.range().first
                }
            }
        }
        return resultingRanges
    }

    private fun mapNumber(number: Long, ranges: List<Pair<Long, LongRange>>): Long {
        val shift = ranges.find { number in it.range() }?.shift() ?: 0
        return number + shift
    }

    private fun Pair<Long, LongRange>.shift() = this.first
    private fun Pair<Long, LongRange>.range() = this.second

}
