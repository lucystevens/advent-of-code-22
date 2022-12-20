package common.threading

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timed

// expected 17984
private val maxValue = 200000L

// testing stuff for threading and coroutines
fun main(){
    //kotlinExampleWithMyFunction()
    //nonThreadedCalculation()
    threadedCalculation()
}

// takes ~10 seconds
private fun nonThreadedCalculation(){
    val (time, primes) = timed {
        (2..maxValue).filter { it.isPrime() }
    }

    println("Non threaded: ${primes.size} primes in $time milliseconds")
}

// takes ~4 seconds
private fun threadedCalculation() {
    val threadCount = 10
    val (time, primes) = timed {
        threaded(threadCount) {
            println("T$it - Starting")
            (it..maxValue step threadCount.toLong())
                .filter { num -> num.isPrime() }
                .also { p -> println("T$it - ${p.size} primes found") }
        }.flatten()
    }

    println("Threaded: ${primes.size} primes in $time milliseconds")
}

private val timeDelay = 100000000000L
private fun kotlinExampleWithMyFunction() {
    val (time, sum) = timed {
        threaded(6) {
            println("Starting $it")
            for(i in 0..timeDelay*it){ i*i*i }
            //delay(timeDelay * it)
            println("Loading $it")
            it
        }.sum()
    }
    println("$sum in ${time}ms")
}

private fun kotlinExample() = runBlocking {
    val deferreds: List<Deferred<Int>> = (1..3).map {
        async {
            println("Starting $it")
            delay(1000L * it)
            println("Loading $it")
            it
        }
    }
    val sum = deferreds.awaitAll().sum()
    println("$sum")
}

private fun Long.isPrime() =
    (2..this/2).none {
        this % it == 0L
    }

fun <T> threaded(threads: Int, fn: suspend (Int) -> T): List<T> =
    runBlocking {
        (0 until threads).map {
            async {
                withContext(Dispatchers.Default){ fn(it) }
            }
        }.awaitAll()
    }