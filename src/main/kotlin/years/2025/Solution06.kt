package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

private typealias RawProblem = Pair<List<String>, Char>
private typealias Problem = Pair<List<Long>, Char>

object Solution06 : Solution<List<RawProblem>>(AOC_YEAR, 6) {
    override fun getInput(handler: InputHandler): List<RawProblem> {
        val data = handler.getInput("\n", false)
        return data.blankColumns.zipWithNext { b1, b2 ->
            val pieces = data.map { row -> row.substring(b1 + 1..<b2) }
            pieces.dropLast(1) to pieces.last().trim().single()
        }
    }

    private val List<String>.blankColumns: List<Int>
        get() {
            val indices = this.first().indices
            val cols = indices.filter { j -> this.all { row -> row[j] == ' ' } }.toMutableList()
            cols.addFirst(-1)
            cols.addLast(indices.last + 1)
            return cols
        }

    private fun List<String>.toLongs() = this.map(String::trim).filter(String::isNotEmpty).map(String::toLong)

    private fun RawProblem.toHumanProblem() = this.first.toLongs() to this.second

    private fun RawProblem.toCephalopodProblem() =
        this.first.first().indices.reversed().map { j -> this.first.map { it[j] }.joinToString("") }
            .toLongs() to this.second

    private val Problem.result
        get() = when (this.second) {
            '+' -> this.first.sum()
            '*' -> this.first.fold(1L, Long::times)
            else -> error("Unexpected operator ${this.second}")
        }

    override fun solve(input: List<RawProblem>): PairOf<Long> {
        return input.sumOf { it.toHumanProblem().result } to
            input.sumOf { it.toCephalopodProblem().result }
    }
}
