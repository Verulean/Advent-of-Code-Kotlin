package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.toPair

object Solution02 : Solution<List<PairOf<Long>>>(AOC_YEAR, 2) {

    override fun getInput(handler: InputHandler) = handler.getInput(",") {
        it.split("-").map(String::toLong).toPair()
    }

    private val Long.digits get() = this.toString().length

    private fun Long.pow(exponent: Int) = generateSequence { this }
        .take(exponent)
        .fold(1L) { acc, elem -> acc * elem }

    private val multiplierCache: MutableMap<PairOf<Int>, Long> = mutableMapOf()
    private val PairOf<Int>.multiplier get() = multiplierCache.getOrPut(this) { (0..<first).sumOf { 10L.pow(second * it) } }

    private fun sumInvalidIds(a: Long, b: Long, numDigits: Int, length: Int): Long {
        if (numDigits % length > 0) return 0L
        val count = numDigits / length
        val multiplier = (count to length).multiplier
        val minChunk = if (numDigits > a.digits) 10L.pow(length - 1) else (a + multiplier - 1) / multiplier
        val maxChunk = if (numDigits < b.digits) 10L.pow(length) - 1 else b / multiplier
        return multiplier * (maxChunk * (maxChunk + 1) - minChunk * (minChunk - 1)) / 2
    }

    private fun sumPairs(a: Long, b: Long, numDigits: Int) = when {
        numDigits % 2 == 0 -> sumInvalidIds(a, b, numDigits, numDigits / 2)
        else -> 0L
    }

    private fun sumRepeats(a: Long, b: Long, numDigits: Int): Long {
        val factors = (1..numDigits / 2).filter { numDigits % it == 0 }
        val counts = factors.associateWith { sumInvalidIds(a, b, numDigits, it) }
        return counts.values.sum() - factors.sumOf { length ->
            factors.takeWhile { it < length }
                .filter { length % it == 0 }
                .sumOf(counts::getValue)
        }
    }

    override fun solve(input: List<PairOf<Long>>): PairOf<Long> {
        var ans1 = 0L
        var ans2 = 0L
        input.forEach { (a, b) ->
            (a.digits..b.digits).forEach {
                ans1 += sumPairs(a, b, it)
                ans2 += sumRepeats(a, b, it)
            }
        }
        return ans1 to ans2
    }
}
