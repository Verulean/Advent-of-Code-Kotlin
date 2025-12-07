package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution

object Solution07 : Solution<List<String>>(AOC_YEAR, 7) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private fun MutableMap<Int, Long>.addBeams(j: Int, count: Long) = this.merge(j, count, Long::plus)

    override fun solve(input: List<String>): Pair<Int, Long> {
        var ans1 = 0
        val beams: MutableMap<Int, Long> = mutableMapOf()
        input.forEach { row ->
            row.withIndex().forEach { (j, c) ->
                when (c) {
                    'S' -> beams[j] = 1
                    '^' -> {
                        val count = beams.getOrDefault(j, 0)
                        if (count > 0) {
                            beams.addBeams(j - 1, count)
                            beams.addBeams(j + 1, count)
                            beams[j] = 0
                            ans1++
                        }
                    }
                }
            }
        }
        return ans1 to beams.values.sum()
    }
}
