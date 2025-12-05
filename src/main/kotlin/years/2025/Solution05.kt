package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.longs
import kotlin.math.max

private typealias Ingredient = Long
private typealias IngredientRange = LongRange

object Solution05 : Solution<Pair<List<IngredientRange>, List<Ingredient>>>(AOC_YEAR, 5) {
    override fun getInput(handler: InputHandler): Pair<List<IngredientRange>, List<Ingredient>> {
        val (fresh, available) = handler.getInput("\n\n")
        return fresh.split('\n')
            .map {
                val (a, b) = it.split('-').map(String::toLong)
                a..b
            } to available.longs()
    }

    private operator fun List<IngredientRange>.contains(n: Ingredient) = this.any { n in it }

    override fun solve(input: Pair<List<IngredientRange>, List<Ingredient>>): Pair<Int, Long> {
        val ranges = input.first.sortedBy { it.first }
        val fresh: MutableList<IngredientRange> = mutableListOf(ranges.first())
        ranges.drop(1).forEach { range ->
            val lastRange = fresh.last()
            if (range.first <= lastRange.last) { // Ranges overlap
                fresh[fresh.lastIndex] = lastRange.first..max(lastRange.last, range.last)
            } else { // Distinct range
                fresh.add(range)
            }
        }
        val ans1 = input.second.count { it in fresh }
        val ans2 = fresh.sumOf { it.last - it.first + 1 }
        return ans1 to ans2
    }
}
