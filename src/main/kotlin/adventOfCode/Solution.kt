package adventOfCode

import kotlin.math.max
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds

abstract class Solution<T>(private val year: Int, private val day: Int) {
    open val inputHandler = InputHandler(year, day)
    abstract fun getInput(handler: InputHandler): T
    abstract fun solve(input: T): Pair<Any?, Any?>
    open fun run(time: Boolean = true, threshold: Long = 500_000_000) {
        var ret: Pair<Any?, Any?> = null to null
        var duration = 0L
        if (time) {
            duration = measureNanoTime {
                repeat(4) {
                    val input = getInput(inputHandler)
                    ret = solve(input)
                }
            }
            val projectedTime = duration / 4
            val trials = max(1, (threshold - duration) / projectedTime).toInt()
            duration += measureNanoTime {
                repeat(trials) {
                    val input = getInput(inputHandler)
                    solve(input)
                }
            }
            duration /= trials + 4
        } else {
            val input = getInput(inputHandler)
            ret = solve(input)
        }
        println(ret.first)
        if (day != 25) println(ret.second)
        if (time) println("Finished execution in ${(duration).nanoseconds}.")
    }
}
