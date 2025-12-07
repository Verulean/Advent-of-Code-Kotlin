package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution

object Solution07 : Solution<Pair<Int, Map<Int, Set<Int>>>>(AOC_YEAR, 7) {
    override fun getInput(handler: InputHandler): Pair<Int, Map<Int, Set<Int>>> {
        val data = handler.getInput("\n")
        val startIndex = data.first().indexOf('S')
        val splitterMap = mutableMapOf<Int, MutableSet<Int>>()
        data.withIndex().forEach { (i, row) ->
            row.withIndex().forEach { (j, c) ->
                if (c == '^') splitterMap.getOrPut(i) { mutableSetOf() }.add(j)
            }
        }
        return startIndex to splitterMap
    }

    private fun MutableMap<Int, Long>.addBeams(j: Int, count: Long) = this.merge(j, count, Long::plus)

    override fun solve(input: Pair<Int, Map<Int, Set<Int>>>): Pair<Int, Long> {
        val (startIndex, splitterMap) = input
        var ans1 = 0
        val beams = splitterMap.entries.sortedBy { it.key }.fold(mapOf(startIndex to 1L)) { acc, (_, splitters) ->
            val newAcc = mutableMapOf<Int, Long>()
            acc.forEach { (j, count) ->
                if (j in splitters) {
                    newAcc.addBeams(j - 1, count)
                    newAcc.addBeams(j + 1, count)
                    ans1++
                } else {
                    newAcc.addBeams(j, count)
                }
            }
            newAcc
        }
        return ans1 to beams.values.sum()
    }
}
