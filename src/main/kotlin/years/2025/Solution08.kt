package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.TripleOf

private typealias JunctionBox = TripleOf<Long>
private typealias Circuit = Set<JunctionBox>
private typealias Graph = MutableMap<JunctionBox, Circuit>

object Solution08 : Solution<Graph>(AOC_YEAR, 8) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map {
        val (x, y, z) = it.split(',').map(String::toLong)
        JunctionBox(x, y, z)
    }.associateWith { setOf(it) }.toMutableMap()

    private const val CONNECTION_COUNT = 1000

    private fun JunctionBox.distanceTo(other: JunctionBox) =
        (this.first - other.first) * (this.first - other.first) +
        (this.second - other.second) * (this.second - other.second) +
        (this.third - other.third) * (this.third - other.third)

    private fun Graph.connect(u: JunctionBox, v: JunctionBox) {
        val component = this.getValue(u).union(this.getValue(v))
        component.forEach { this[it] = component }
    }

    private val Graph.circuits: List<Circuit>
        get() {
            val junctions = this.keys.toMutableSet()
            val circuits: MutableList<Circuit> = mutableListOf()
            while (junctions.isNotEmpty()) {
                val circuit = this.getValue(junctions.first())
                circuits.add(circuit)
                junctions.removeAll(circuit)
            }
            return circuits
        }

    private val Graph.isConnected get() = this.values.first().size == this.size

    override fun solve(input: Graph): PairOf<Long> {
        val connections = input.keys.withIndex()
            .flatMap { (i, u) -> input.keys.drop(i + 1).map(u::to) }
            .sortedBy { (u, v) -> u.distanceTo(v) }
        connections.take(CONNECTION_COUNT).forEach { (u, v) -> input.connect(u, v) }
        val ans1 = input.circuits.map { it.size.toLong() }.sortedDescending().take(3).reduce(Long::times)
        val lastConnection = connections.drop(CONNECTION_COUNT).asSequence().takeWhile { !input.isConnected }
            .map { it.also { (u, v) -> input.connect(u, v) } }.last()
        return ans1 to lastConnection.first.first * lastConnection.second.first
    }
}
