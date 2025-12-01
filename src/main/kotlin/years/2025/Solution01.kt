package years.`2025`

import adventOfCode.*
import adventOfCode.util.*
import kotlin.math.abs

object Solution01 : Solution<List<Int>>(AOC_YEAR, 1) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n") {
        it.replace('L', '-')
            .replace('R', '+')
            .toInt()
    }

    private fun Int.floorMod(other: Int) = (this % other + other) % other

    override fun solve(input: List<Int>): PairOf<Int> {
        var ans1 = 0
        var ans2 = 0
        input.fold(50) { position, offset ->
            val rotated = position + offset
            val newPosition = rotated.floorMod(100)

            // Part 1
            if (newPosition == 0) ans1++
            // Part 2
            var rotations = abs(rotated / 100)
            if (offset < 0 && position != 0 && rotated <= 0) rotations++
            ans2 += rotations

            newPosition
        }
        return ans1 to ans2
    }
}
