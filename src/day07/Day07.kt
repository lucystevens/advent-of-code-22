package day07

import common.Challenge
import common.Part
import common.tests
import print

class Day07 : Challenge<AocDirectory, Long>() {
    override val day: String = "day07"

    override fun part1(input: AocDirectory): Long {
        return input.getAllFiles()
            .filter { it.type == "dir" }
            .map { it.size() }
            .filter { it < 100000 }
            .sum()
    }

    private val totalSpace = 70000000
    private val spaceNeeded = 30000000

    override fun part2(input: AocDirectory): Long {
        val spaceAvailable = totalSpace - input.size()
        val toDelete = spaceNeeded - spaceAvailable
        return input.getAllFiles()
            .filter { it.type == "dir" }
            .map { it.size() }
            .filter { it >= toDelete }
            .minOrNull()!!
    }

    override fun parseInput(lines: List<String>): AocDirectory {
        return lines.toFileSystem()
    }

    override val tests = tests<Long> {
        file("test", part1 = 95437, part2 = 24933642)
        file("input", part1 = 1886043, part2 = 3842121)
    }

}

fun main() {
    Day07().test(Part.BOTH)
}
