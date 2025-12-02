package years.`2025`

import adventOfCode.*
import adventOfCode.util.*

object Solution02 : Solution<List<PairOf<Long>>>(AOC_YEAR, 2) {

    override fun getInput(handler: InputHandler) = handler.getInput(",") {
        it.split("-").map(String::toLong).zipWithNext().first()
    }

    private val PairOf<Int>.multiplier get() = ("0".repeat(second - 1) + "1").repeat(first).toLong()

    private val multiplierCache: MutableMap<Pair<Int, Boolean>, List<Long>> = mutableMapOf()
    private fun getMultipliers(length: Int, checkFactors: Boolean) = multiplierCache.getOrPut(length to checkFactors) {
        val range = if (checkFactors) 2..length else 2..2
        range.filter { length % it == 0 }
            .map { (it to length / it).multiplier }
    }

    private fun countInvalidIds(start: Long, end: Long, checkFactors: Boolean) = (start..end)
        .filter { id ->
            getMultipliers(id.toString().length, checkFactors).any { id % it == 0L }
        }.sum()

    override fun solve(input: List<PairOf<Long>>): PairOf<Long> {
        val ans1 = input.sumOf { countInvalidIds(it.first, it.second, false) }
        val ans2 = input.sumOf { countInvalidIds(it.first, it.second, true) }
        return ans1 to ans2
    }
}
