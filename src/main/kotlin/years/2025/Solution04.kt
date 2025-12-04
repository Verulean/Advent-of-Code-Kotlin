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

    private fun MutableSet<Point2D>.pop(): Point2D {
        val ret = this.first()
        this.remove(ret)
        return ret
    }

    private fun MutableSet<Point2D>.removeRolls() {
        this.removeAll(this.filter { p -> directions.count { p + it in this } < 4 })
    }

    private fun MutableSet<Point2D>.removeAllRolls() {
        val queue = this.toMutableSet()
        while (queue.isNotEmpty()) {
            val p = queue.pop()
            val neighbors = directions.map(p::plus).filter(this::contains).toList()
            if (neighbors.size < 4) {
                this.remove(p)
                queue.addAll(neighbors)
            }
        }
    }

    override fun solve(input: Set<Point2D>): PairOf<Int> {
        val rolls = input.toMutableSet()
        val startingCount = rolls.size
        rolls.removeRolls()
        val ans1 = startingCount - rolls.size
        rolls.removeAllRolls()
        val ans2 = startingCount - rolls.size
        return ans1 to ans2
    }
}
