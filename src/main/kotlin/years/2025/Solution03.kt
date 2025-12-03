package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

private typealias Bank = List<Int>

object Solution03 : Solution<List<Bank>>(AOC_YEAR, 3) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n") { it.map(Char::digitToInt) }

    private fun Bank.joltage(numDigits: Int): Long {
        val endOffset = this.size - numDigits
        var startIdx = 0
        return (0..<numDigits)
            .map(endOffset::plus)
            .fold(0L) { acc, endIdx ->
                var digit = -1
                (startIdx..endIdx).forEach {
                    val n = this[it]
                    if (n > digit) {
                        digit = n
                        startIdx = it + 1
                    }
                }
                10 * acc + digit
            }
    }

    override fun solve(input: List<Bank>): PairOf<Long> {
        return input.sumOf { it.joltage(2) } to input.sumOf { it.joltage(12) }
    }
}
