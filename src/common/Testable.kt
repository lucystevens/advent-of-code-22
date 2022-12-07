package common

interface Testable {
    fun test(toRun: Part = Part.BOTH)
}