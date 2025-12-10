package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.ints
import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.Status
import kotlin.math.min

private typealias Machine = Triple<List<Int>, List<List<Int>>, List<Int>>

object Solution10 : Solution<List<Machine>>(AOC_YEAR, 10) {
    private fun String.toMachine(): Machine {
        var lights: List<Int> = listOf()
        val wirings: MutableList<List<Int>> = mutableListOf()
        var joltages: List<Int> = listOf()
        this.split(' ').forEach {
            val body = it.drop(1).dropLast(1)
            when (it.first()) {
                '[' -> lights = body.replace('.', '0').replace('#', '1').map(Char::digitToInt)
                '(' -> wirings.add(body.ints())
                '{' -> joltages = body.ints()
            }
        }
        return Triple(lights, wirings, joltages)
    }

    override fun getInput(handler: InputHandler) = handler.getInput("\n") { it.toMachine() }

    private val Machine.lightPresses
        get(): Int {
            val (lights, wirings, _) = this
            val target = lights.joinToString("").reversed().toInt(2)
            val nums = wirings.map { it.sumOf { i -> 1 shl i } }
            var ret = Int.MAX_VALUE
            val maxIndex = wirings.fold(1) { acc, _ -> acc * 2 }
            (0..<maxIndex).map { n -> wirings.indices.map { n and (1 shl it) > 0 } }.forEach { presses ->
                val state = presses.zip(nums).filter { it.first }.map { it.second }.fold(0, Int::xor)
                if (state == target) ret = min(ret, presses.count { it })
            }
            assert(ret != Int.MAX_VALUE)
            return ret
        }

    private val Machine.joltagePresses
        get(): Int {
            val (_, wirings, joltages) = this
            val context = Context()
            val solver = context.mkSolver()

            // Build system of equations
            val zero = context.mkInt(0)
            val vars: MutableList<IntExpr> = mutableListOf()
            val joltSums: List<MutableList<IntExpr>> = joltages.map { mutableListOf() }
            wirings.withIndex().forEach { (i, button) ->
                val x = context.mkIntConst("x$i")
                vars.add(x)
                solver.add(context.mkGe(x, zero))
                button.forEach { j -> joltSums[j].add(x) }
            }
            joltages.zip(joltSums).forEach { (n, xs) ->
                val xSum = context.mkAdd(*xs.toTypedArray())
                solver.add(context.mkEq(xSum, context.mkInt(n)))
            }
            val total = context.mkIntConst("total")
            solver.add(context.mkEq(total, context.mkAdd(*vars.toTypedArray())))

            // Minimize solution
            var ret = Int.MAX_VALUE
            while (solver.check() == Status.SATISFIABLE) {
                val count = solver.model.eval(total, true).toString().toInt()
                solver.add(context.mkLt(total, context.mkInt(count)))
                ret = min(ret, count)
            }
            assert(ret != Int.MAX_VALUE)
            return ret
        }

    override fun solve(input: List<Machine>): PairOf<Int> {
        val ans1 = input.sumOf { it.lightPresses }
        val ans2 = input.sumOf { it.joltagePresses }
        return ans1 to ans2
    }
}
