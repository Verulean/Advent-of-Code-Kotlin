package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

private typealias ReactorGraph = Map<String, List<String>>

object Solution11 : Solution<ReactorGraph>(AOC_YEAR, 11) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").associate {
        val (source, destinations) = it.split(": ")
        source to destinations.split(" ")
    }

    private val pathMemo: MutableMap<Pair<String, String>, Long> = mutableMapOf()
    private fun ReactorGraph.countPaths(node: String, end: String): Long = pathMemo.getOrPut(node to end) {
        when (node) {
            end -> 1
            else -> this[node]?.sumOf { this.countPaths(it, end) } ?: 0
        }
    }

    override fun solve(input: ReactorGraph): PairOf<Long> {
        val ans1 = input.countPaths("you", "out")
        val ans2 = when (val dac2fft = input.countPaths("dac", "fft")) {
            0L -> input.countPaths("svr", "fft") * input.countPaths("fft", "dac") * input.countPaths("dac", "out")
            else -> input.countPaths("svr", "dac") * dac2fft * input.countPaths("fft", "out")
        }
        return ans1 to ans2
    }
}
