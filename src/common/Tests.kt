package common

import truncate
import java.nio.file.Paths
import kotlin.io.path.readLines

data class Test<R>(
    val data: String,
    val name: String,
    val type: TestType,
    val part1: R?,
    val part2: R?,
    val ignore: Boolean
){

    fun getInput(day: String): List<String> = when(type){
            TestType.FILE -> Paths.get("src/$day/$data.txt").readLines()
            TestType.STRING -> listOf(data)
        }

}

fun <R> tests(testDefinition: Tests<R>.() -> Unit): List<Test<R>> =
    Tests<R>().apply(testDefinition).tests


class Tests<R> {
    val tests = mutableListOf<Test<R>>()
    fun file(fileName: String, part1: R? = null, part2: R? = null, ignore: Boolean = false) {
        tests.add(Test(fileName, fileName, TestType.FILE, part1, part2, ignore))
    }

    fun string(
        input: String,
        name: String = input.truncate(20),
        part1: R? = null,
        part2: R? = null,
        ignore: Boolean = false
    ) {
        tests.add(Test(input, name, TestType.STRING, part1, part2, ignore))
    }
}

enum class Part(
    val runPart1: Boolean,
    val runPart2: Boolean
) {
    ONE(true, false),
    TWO(false, true),
    BOTH(true, true)
}

enum class TestType {
    FILE,
    STRING
}

