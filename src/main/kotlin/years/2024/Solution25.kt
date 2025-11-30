package years.`2024`

import adventOfCode.InputHandler
import adventOfCode.Solution

object Solution25 : Solution<Collection<String>>(AOC_YEAR, 25) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n\n")

    private fun String.toBitVec() = this.map { if (it == '#') '1' else '0' }.joinToString("").toLong(2)

    override fun solve(input: Collection<String>): Pair<Int, Nothing?> {
        val (locks, keys) = input.map { it.toBitVec() }.partition { it and 1L == 0L }
        return locks.sumOf { lock -> keys.filter { key -> key and lock == 0L }.size } to null
    }
}
