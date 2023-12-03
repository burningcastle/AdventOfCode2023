package days

import Day
import java.io.File

class Day3 : Day {

    override fun run() {
        val lines = File("src/main/resources/Day3.txt").readText().lines()

        // Part 1
        val partNumbers = lines.flatMapIndexed { rowIndex, line ->
            var currentNumber = ""
            line.mapIndexedNotNull { colIndex, char ->
                var partNumber: Int? = null
                if (char.isDigit()) currentNumber += char
                val nextIsDigit = lines[rowIndex].getOrNull(colIndex + 1)?.isDigit() ?: false
                if (!nextIsDigit && currentNumber.isNotEmpty()) {
                    // found a complete number, now check if it is a part number
                    val indexRange = maxOf(0, colIndex - currentNumber.length)..minOf(colIndex + 1, line.lastIndex)
                    val hasSymbolAbove = lines.getOrNull(rowIndex - 1)?.substring(indexRange)?.containsSymbol() ?: false
                    val hasSymbolLeftOrRight = line.substring(indexRange).containsSymbol()
                    val hasSymbolBelow = lines.getOrNull(rowIndex + 1)?.substring(indexRange)?.containsSymbol() ?: false
                    if (hasSymbolAbove || hasSymbolLeftOrRight || hasSymbolBelow) {
                        partNumber = currentNumber.toInt()
                    }
                    currentNumber = ""
                }
                partNumber
            }
        }
        println("Part 1: " + partNumbers.sum()) // 551094

        // Part 2
        val gearRatios = lines.flatMapIndexed { rowIndex, line ->
            line.mapIndexedNotNull { colIndex, char ->
                var gearRatio: Int? = null
                if (char == '*') {
                    val neighbors = mutableListOf<String>()
                    // same row
                    val left = line.substring(0, colIndex).takeLastWhile { it.isDigit() }
                    val right = line.drop(colIndex + 1).takeWhile { it.isDigit() }
                    neighbors.add(left)
                    neighbors.add(right)
                    // above
                    if (rowIndex - 1 in lines.indices) {
                        val topLeft = lines[rowIndex - 1].substring(0, colIndex).takeLastWhile { it.isDigit() }
                        val topRight = lines[rowIndex - 1].drop(colIndex + 1).takeWhile { it.isDigit() }
                        if (lines[rowIndex - 1][colIndex].isDigit()) {
                            val above = topLeft + lines[rowIndex - 1][colIndex] + topRight
                            neighbors.add(above)
                        } else {
                            neighbors.add(topLeft)
                            neighbors.add(topRight)
                        }
                    }
                    // below
                    if (rowIndex + 1 in lines.indices) {
                        val bottomLeft = lines[rowIndex + 1].substring(0, colIndex).takeLastWhile { it.isDigit() }
                        val bottomRight = lines[rowIndex + 1].drop(colIndex + 1).takeWhile { it.isDigit() }
                        if (lines[rowIndex + 1][colIndex].isDigit()) {
                            val below = bottomLeft + lines[rowIndex + 1][colIndex] + bottomRight
                            neighbors.add(below)
                        } else {
                            neighbors.add(bottomLeft)
                            neighbors.add(bottomRight)
                        }
                    }
                    val adjacentPartNumbers = neighbors.filter { it.isNotEmpty() }
                    if (adjacentPartNumbers.size == 2) {
                        gearRatio = adjacentPartNumbers.first().toInt() * adjacentPartNumbers.last().toInt()
                    }
                }
                gearRatio
            }
        }
        println("Part 2: " + gearRatios.sum()) // 80179647
    }

    private fun String.containsSymbol(): Boolean = this.any { !it.isDigit() && it != '.' }

}
