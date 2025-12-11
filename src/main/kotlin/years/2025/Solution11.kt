package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution

private typealias ReactorGraph = Map<String, List<String>>

object Solution11 : Solution<ReactorGraph>(AOC_YEAR, 11) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").associate {
        val (source, destinations) = it.split(": ")
        source to destinations.split(" ")
    }

    private val p1Memo: MutableMap<String, Int> = mutableMapOf()
    private fun ReactorGraph.countPaths(node: String): Int = p1Memo.getOrPut(node) {
        when (node) {
            "out" -> 1
            else -> this[node]?.sumOf { this.countPaths(it) } ?: 0
        }
    }

    private val p2Memo: MutableMap<Triple<String, Boolean, Boolean>, Long> = mutableMapOf()
    private fun ReactorGraph.countPaths(node: String, dac: Boolean, fft: Boolean): Long =
        p2Memo.getOrPut(Triple(node, dac, fft)) {
            when (node) {
                "out" -> if (dac && fft) 1 else 0
                "dac" -> this[node]?.sumOf { this.countPaths(it, true, fft) } ?: 0
                "fft" -> this[node]?.sumOf { this.countPaths(it, dac, true) } ?: 0
                else -> this[node]?.sumOf { this.countPaths(it, dac, fft) } ?: 0
            }
        }

    override fun solve(input: ReactorGraph): Pair<Int, Long> {
        return input.countPaths("you") to input.countPaths("svr", dac = false, fft = false)
    }
}
