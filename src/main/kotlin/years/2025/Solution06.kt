package years.`2025`

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

object Solution06 : Solution<List<String>>(AOC_YEAR, 6) {
    override fun getInput(handler: InputHandler): List<String> {
        val data = handler.getInput("\n", false) { "$it " }
        return data.first().indices.map { j -> data.map { it[j] }.joinToString("") }
    }

    override fun solve(input: List<String>): PairOf<Long> {
        var ans1 = 0L
        var ans2 = 0L
        val humanNumbers = MutableList(input.first().length - 1) { 0L }
        var cephalopodResult = 0L
        var op: (Long, Long) -> Long = Long::plus
        input.forEach { column ->
            if (column.isBlank()) {
                ans1 += humanNumbers.reduce(op)
                ans2 += cephalopodResult
                humanNumbers.replaceAll { 0 }
                return@forEach
            }
            var cephalopodNumber = 0L
            column.withIndex().forEach { (i, c) ->
                when (c) {
                    '+' -> {
                        op = Long::plus
                        cephalopodResult = 0L
                    }

                    '*' -> {
                        op = Long::times
                        cephalopodResult = 1L
                    }

                    ' ' -> {}
                    else -> {
                        val d = c.digitToInt()
                        humanNumbers[i] = humanNumbers[i] * 10 + d
                        cephalopodNumber = cephalopodNumber * 10 + d
                    }
                }
            }
            cephalopodResult = op(cephalopodResult, cephalopodNumber)
        }
        return ans1 to ans2
    }
}
