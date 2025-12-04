package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus

object Solution04 : Solution<Set<Point2D>>(AOC_YEAR, 4) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").withIndex().flatMap { (i, row) ->
        row.withIndex().filter { it.value == '@' }.map { i to it.index }
    }.toSet()

    private val directions = sequenceOf(-1 to -1, -1 to 0, -1 to 1, 0 to 1, 1 to 1, 1 to 0, 1 to -1, 0 to -1)

    private fun MutableSet<Point2D>.removeRolls(): Int {
        val toRemove = this.filter { p -> directions.count { p + it in this } < 4 }
        this.removeAll(toRemove)
        return toRemove.size
    }

    override fun solve(input: Set<Point2D>): PairOf<Int> {
        val rolls = input.toMutableSet()
        val ans1 = rolls.removeRolls()
        val ans2 = ans1 + generateSequence { rolls.removeRolls() }.takeWhile { it > 0 }.sum()
        return ans1 to ans2
    }
}
