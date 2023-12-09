package days

import Day
import java.io.File

class Day7 : Day {

    override fun run() {
        val lines = File("src/main/resources/Day7.txt").readText().lines()
        val handsAndBids = lines.map { it.split(" ") }.map { it[0] to it[1].toLong() }

        // Part 1
        val sortedHandsWithoutJokers = handsAndBids.sortedBy { Hand(it.first, withJokers = false) }
        println("Part 1: " + sortedHandsWithoutJokers
            .mapIndexed { i, pair -> pair.second * (i + 1) }
            .sum()) // 6440 // 252295678

        // Part 2
        val sortedHandsWithJokers = handsAndBids.sortedBy { Hand(it.first, withJokers = true) }
        println("Part 2: " + sortedHandsWithJokers
            .mapIndexed { i, pair -> pair.second * (i + 1) }
            .sum()) // 5905 // 250577259
    }

    private class Hand(val hand: String, val withJokers: Boolean) : Comparable<Hand> {
        override fun compareTo(other: Hand): Int {
            val firstOrdering = this.rank().compareTo(other.rank())
            return if (firstOrdering != 0) {
                firstOrdering
            } else {
                // apply second rule
                val cards = if (withJokers) {
                    listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
                } else {
                    listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
                }
                val cardByCardComparison =
                    hand.mapIndexed { index, c -> cards.indexOf(other.hand[index]).compareTo(cards.indexOf(c)) }
                cardByCardComparison.first { it != 0 }
            }
        }

        // Every hand is exactly one type. From strongest to weakest, they are:
        private fun rank(): Int {
            val cardFrequency = hand.fold(mutableMapOf<Char, Int>()) { map, char ->
                map[char] = map.getOrDefault(char, 0) + 1
                map
            }
            val jokerCount = if (withJokers) cardFrequency.getOrDefault('J', 0) else 0
            if (withJokers) cardFrequency.remove('J')
            val cardsFrequencyDescending = cardFrequency.entries.sortedByDescending { it.value }
//            Five of a kind, where all five cards have the same label: AAAAA
            if (jokerCount == 5 || cardsFrequencyDescending[0].value + jokerCount == 5) return 7
//            Four of a kind, where four cards have the same label and one card has a different label: AA8AA
            if (cardsFrequencyDescending[0].value + jokerCount == 4) return 6
//            Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
            if (cardsFrequencyDescending[0].value == 3 + jokerCount && cardsFrequencyDescending[1].value == 2 ||
                cardsFrequencyDescending[0].value == 3 && cardsFrequencyDescending[1].value + jokerCount == 2 ||
                cardsFrequencyDescending[0].value == 2 && cardsFrequencyDescending[1].value == 1 && jokerCount == 2 ||
                cardsFrequencyDescending[0].value == 2 && cardsFrequencyDescending[1].value == 2 && jokerCount == 1
            ) return 5
//            Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
            if (cardsFrequencyDescending[0].value + jokerCount == 3) return 4
//            Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
            if (cardsFrequencyDescending[0].value == 2 && cardsFrequencyDescending[1].value == 2 ||
                cardsFrequencyDescending[0].value == 1 && cardsFrequencyDescending[1].value == 1 && jokerCount == 2
            ) return 3
//            One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
            if (cardsFrequencyDescending[0].value == 2 ||
                cardsFrequencyDescending[0].value == 1 && jokerCount == 1
            ) return 2
//            High card, where all cards' labels are distinct: 23456
            return 1
        }
    }
}
