package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.*
import kotlin.math.abs

private typealias Tile = PairOf<Long>
private val Tile.x get() = this.first
private val Tile.y get() = this.second
private typealias Edge = PairOf<Tile>

class Polygon(val points: List<Tile>) {
    private val boundary: MutableSet<Tile> = mutableSetOf()
    private val horizontalEdges: MutableSet<Edge> = mutableSetOf()
    private val verticalEdges: MutableSet<Edge> = mutableSetOf()
    private val rayCaster: MutableMap<Long, MutableList<Long>> = mutableMapOf()

    init {
        this.points.pairs().forEach { (p1, p2) ->
            val (x1, x2) = listOf(p1.x, p2.x).sorted()
            val (y1, y2) = listOf(p1.y, p2.y).sorted()
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
        val p3 = p1.x to p2.y
        val p4 = p2.x to p1.y
        return sequenceOf(p3, p4).all(this::containsPoint)
            && sequenceOf(p1 to p4, p4 to p2, p2 to p3, p3 to p1).none(this::intersectsBoundary)
    }
}

object Solution09 : Solution<Polygon>(AOC_YEAR, 9) {
    override fun getInput(handler: InputHandler) = Polygon(handler.getInput("\n") { it.longs().toPair() })

    override fun solve(input: Polygon): PairOf<Long> {
        var ans1 = 0L
        var ans2 = 0L
        input.points.combinations(2).forEach { (p1, p2) ->
            val area = (abs(p2.x - p1.x) + 1) * (abs(p2.y - p1.y) + 1)
            if (area > ans1) ans1 = area
            if (area > ans2 && input.containsRectangle(p1, p2)) ans2 = area
        }
        return ans1 to ans2
    }
}
