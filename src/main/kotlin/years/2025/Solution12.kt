package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution

object Solution12 : Solution<List<String>>(AOC_YEAR, 12) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n\n")

    override fun solve(input: List<String>): Pair<Int, Nothing?> {
        // Get present dimensions
        val (x, y) = input.dropLast(1).map {
            val lines = it.split("\n").drop(1)
            lines.size to lines.first().length
        }.toSet().single()
        // Count valid arrangements
        return input.last().split("\n").count {
            val (area, counts) = it.split(": ")
            val (width, height) = area.split('x').map(String::toInt)
            val required = counts.split(' ').sumOf(String::toInt)
            required <= (width / x) * (height / y)
        } to null
    }
}
