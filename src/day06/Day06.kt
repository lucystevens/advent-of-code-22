package day06

import common.Challenge

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

}

fun main() {
    val day06 = Day06()
    day06.runTests(listOf(
        "bvwbjplbgvbhsrlpgdmjqwftvncz" to 5,
        "nppdvjthqldpwncqszvftbrmjlhg" to 6,
        "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 10,
        "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 11
    ), day06::part1)
    day06.testPart1(7, solution = 1920)

    day06.runTests(listOf(
        "bvwbjplbgvbhsrlpgdmjqwftvncz" to 23,
        "nppdvjthqldpwncqszvftbrmjlhg" to 23,
        "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 29,
        "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 26
    ), day06::part2)
    day06.testPart2(19, solution = 2334)
}
