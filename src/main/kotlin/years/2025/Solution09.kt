package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.longs
import kotlin.math.abs

private typealias Tile = PairOf<Long>
private typealias Edge = PairOf<Tile>

class Polygon(val points: List<Tile>) {
    private val boundary: MutableSet<Tile> = mutableSetOf()
    private val horizontalEdges: MutableSet<Edge> = mutableSetOf()
    private val verticalEdges: MutableSet<Edge> = mutableSetOf()
    private val rayCaster: MutableMap<Long, MutableList<Long>> = mutableMapOf()

    init {
        val edges = this.points.zipWithNext().asSequence() + sequenceOf(this.points.last() to this.points.first())
        edges.forEach { (p1, p2) ->
            val (x1, x2) = listOf(p1.first, p2.first).sorted()
            val (y1, y2) = listOf(p1.second, p2.second).sorted()
            if (x1 == x2) { // vertical edge
                this.boundary.addAll((y1..<y2).map(x1::to))
                this.verticalEdges.add(p1 to p2)
                (y1..<y2).forEach { this.rayCaster.getOrPut(it) { mutableListOf() }.add(x1) }
            } else { // horizontal edge
                this.boundary.addAll((x1..<x2).map { it to y1 })
                this.horizontalEdges.add(p1 to p2)
            }
        }
        this.rayCaster.values.forEach { it.sort() }
    }

    private fun containsPoint(p: Tile): Boolean {
        if (p in this.boundary) return true
        val (px, py) = p
        return this.rayCaster.getOrDefault(py, listOf()).takeWhile { it < px }.fold(false) { acc, _ -> !acc }
    }

    private fun intersectsBoundary(edge: Edge): Boolean {
        val (x1, y1) = edge.first
        val (x2, y2) = edge.second
        // this doesn't work in general, but the input is nice enough for it to suffice.
        return if (x1 == x2) { // vertical
            this.horizontalEdges.any {
                val (x3, y3) = it.first
                val (x4, _) = it.second
                (x3 - x1) * (x4 - x1) < 0 && (y1 - y3) * (y2 - y3) < 0
            }
        } else { // horizontal
            this.verticalEdges.any {
                val (x3, y3) = it.first
                val (_, y4) = it.second
                (y3 - y1) * (y4 - y1) < 0 && (x1 - x3) * (x2 - x3) < 0
            }
        }
    }

    fun containsRectangle(p1: Tile, p2: Tile): Boolean {
        val p3 = p1.first to p2.second
        val p4 = p2.first to p1.second
        return sequenceOf(p3, p4).all(this::containsPoint) && sequenceOf(
            p1 to p4, p4 to p2, p2 to p3, p3 to p1
        ).all { !this.intersectsBoundary(it) }
    }
}

object Solution09 : Solution<Polygon>(AOC_YEAR, 9) {
    override fun getInput(handler: InputHandler) = Polygon(handler.getInput("\n") { it.longs().zipWithNext().first() })

    override fun solve(input: Polygon): PairOf<Long> {
        var ans1 = 0L
        var ans2 = 0L
        input.points.withIndex().flatMap { (i, p1) -> input.points.drop(i + 1).map(p1::to) }.forEach { (p1, p2) ->
            val area = (abs(p2.first - p1.first) + 1L) * (abs(p2.second - p1.second) + 1L)
            if (area > ans1) ans1 = area
            if (area > ans2 && input.containsRectangle(p1, p2)) ans2 = area
        }
        return ans1 to ans2
    }
}
