package day06

import common.Challenge
import common.tests

class Day06 : Challenge<String, Int>() {
    override val day: String = "day06"

    override fun part1(input: String): Int = input.solve(4)

    override fun part2(input: String): Int = input.solve(14)

    private fun String.solve(num: Int): Int {
        forEachIndexed { idx, _ ->
            if(idx >= num-1 &&
                substring(idx-(num-1), idx+1)
                    .toSet()
                    .size == num)
                return idx+1
        }
        return -1
    }

    override fun parseInput(lines: List<String>): String {
        return lines[0]
    }

    override val tests = tests<Int> {
        file("test", part1 = 7, part2 = 19)
        string("bvwbjplbgvbhsrlpgdmjqwftvncz", part1 = 5, part2 = 23)
        string("nppdvjthqldpwncqszvftbrmjlhg", part1 = 6, part2 = 23)
        string("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", part1 = 10, part2 = 29)
        string("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", part1 = 11, part2 = 26)
        file("input", part1 = 1920, part2 = 2334)
    }

}

fun main() {
    Day06().test()
}
