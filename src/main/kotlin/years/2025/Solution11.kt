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

    private fun ReactorGraph.countPaths(steps: Collection<String>) =
        steps.zipWithNext { node, end -> this.countPaths(node, end) }.reduce(Long::times)

    override fun solve(input: ReactorGraph): PairOf<Long> {
        val ans1 = input.countPaths("you", "out")
        val ans2 = input.countPaths(listOf("svr", "dac", "fft", "out")) +
            input.countPaths(listOf("svr", "fft", "dac", "out"))
        return ans1 to ans2
    }
}
