import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.math.abs
import kotlin.math.exp

// Converts string to md5 hash.
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

// Checks 2 objects are equal
fun <R> checkAnswer(actual: R, expected: R, name: String = "Testing") {
    check(actual == expected) { "$name: Expected: $expected, Actual val: $actual" }
    println("$name: Success ($actual)")
}

// converts a list of 2 items to a pair
fun <T> List<T>.toPair(): Pair<T,T> = Pair(get(0), get(1))

// moves every item in a list to the left
fun <T> List<T>.rotateLeft(delta: Int = 1) =
    rotate(delta * -1)

// moves every item in a list to the right
fun <T> List<T>.rotateRight(delta: Int = 1) =
    rotate(delta)

// modifies indexes by delta, minus to rotate left, plus to rotate right
fun <T> List<T>.rotate(delta: Int): List<T> {
    // make sure a very low delta won't cause issues
    val buffer = (abs(delta/size)+1)*size
    return List(size) { idx ->
        val rotatedIdx = (idx + buffer + (-1*delta)) % size
        get(rotatedIdx)
    }
}

// performs a mapping on both elements of a pair
fun <T, R> Pair<T,T>.mapBoth(mappingFunction: (T) -> R): Pair<R,R> {
    return Pair(mappingFunction.invoke(first), mappingFunction.invoke(second))
}

// performs a mapping on the first element of a pair
fun <T, R> Pair<T,T>.mapFirst(mappingFunction: (T) -> R): Pair<R, T> {
    return Pair(mappingFunction.invoke(first), second)
}

// performs a mapping on the second element of a pair
fun <T, R> Pair<T,T>.mapSecond(mappingFunction: (T) -> R): Pair<T,R> {
    return Pair(first, mappingFunction.invoke(second))
}

// Combines every value from this list, with every value from the other list
fun <T,U,V> Iterable<T>.combine(iterable: Iterable<U>, combiner: (T,U) -> V): List<V>{
    return flatMap { t ->
        iterable.map { u -> combiner.invoke(t,u) }
    }
}

// groups a simple list into groups of x
fun <T> List<T>.groupByLines(linesPerGroup: Int): List<List<T>> =
    foldIndexed(mutableListOf<MutableList<T>>()){ idx, res, line ->
        val group = idx / linesPerGroup
        res.apply {
            getOrAdd(group) { mutableListOf() }
                .add(line)
        }
    }

// adds computed defaultVal elements until list large enough for index
fun <T> MutableList<T>.getOrAdd(index: Int, defaultValFn: (Int)->T): T {
    while(size<=index) add(defaultValFn(size-1))
    return get(index)
}


// finds the intersecting set of characters between 2 strings
fun String.intersect(other: String): Set<Char> =
    toCharArray()
        .intersect(other
            .toCharArray()
            .asIterable()
            .toSet())

// inline print function for debugging
fun <T, C : Iterable<T>> C.print(): C =
    onEach { println(it) }

// another inline print function for debugging
fun <T> T.alsoPrint(): T = also { println(it) }

// do a thing x times
fun repeat(times: Int, thing: (Int) -> Unit) =
    (1..times).forEach(thing)

// truncate a string to a maximum length
fun String.truncate(maxLength: Int) =
    substring(0, min(maxLength, length))

// uppercases the first letter of a String
fun String.sentenceCase() =
    first().uppercase() + drop(1)

// Converts a list to a stack
fun <T> Iterable<T>.toStack(): Stack<T> =
    fold(Stack<T>()){ stack, item ->
        stack.apply { push(item) }
    }

// pops multiple items from a stack, retaining order
fun <T> Stack<T>.multiPop(num: Int): List<T> =
    (1..num).map {
        pop()
    }.reversed()

// update a list in-place
fun <T> MutableList<T>.update(index: Int, doUpdate: (T) -> T): MutableList<T> = apply {
    val item = get(index)
    set(index, doUpdate(item))
}

// get min and max in single function
fun <T, R : Comparable<R>> Iterable<T>.minMaxOf(selector: (T) -> R): Pair<R,R> {
    val iter = iterator()
    if (!iter.hasNext()) throw NoSuchElementException()
    var max = selector(iter.next())
    var min = max
    while (iter.hasNext()) {
        val v = selector(iter.next())
        max = max(max, v)
        min = min(min, v)
    }
    return min to max
}

// get max of 2 comparables
fun <T: Comparable<T>> max(t1: T, t2: T): T =
    if(t1 > t2) t1 else t2

// get min of 2 comparables
fun <T: Comparable<T>> min(t1: T, t2: T): T =
    if(t1 < t2) t1 else t2

// sublist until
fun <T> Iterable<T>.until(until: (T) -> Boolean): List<T>{
    return fold(mutableListOf()){ list, item ->
        if(until(item)) return list
        else list.add(item)
        list
    }
}

// compute factorial (e.g. !)
fun Int.factorial(): Long =
    (1..this).fold(1L){ total, num ->
        total * num
    }

// compute number of ways of choosing x numbers from this
fun Int.choose(nums: Int): Long =
    factorial() / (nums.factorial() * (this-nums).factorial())

// remove first instance of an element from a list (and return)
fun <T> MutableCollection<T>.removeFirstWhere(predicate: (T) -> Boolean): T =
    first(predicate).apply { remove(this) }

// check if a string contains all characters (order independent)
fun String.containsAllChars(chars: String): Boolean =
    chars.all { contains(it) }

// merges 2 maps by combining values
fun <K, V> Map<K, V>.merge(other: Map<K, V>, mergeFn: (V,V) -> V): MutableMap<K, V> {
    return toMutableMap().apply {
        other.forEach {
            merge(it.key, it.value!!) { old, _ -> mergeFn(old, it.value) }
        }
    }
}

// convert a pair to a range
fun <T: Comparable<T>> Pair<T,T>.toRange(): ClosedRange<T> = (first..second)

// checks if a range intersects another range
fun <T: Comparable<T>, Range: ClosedRange<T>> Range.intersects(other: Range): Boolean {
    return contains(other.start) || contains(other.endInclusive) || other.contains(start) || other.contains(endInclusive)
}

// checks if a range entirely contains another range
fun <T: Comparable<T>, Range: ClosedRange<T>> Range.containsRange(other: Range): Boolean {
    return contains(other.start) && contains(other.endInclusive)
}

// This assumes intersection
fun <T: Comparable<T>, Range: ClosedRange<T>> Range.findOverlap(range: Range): ClosedRange<T> =
    (max(this.start, range.start)..min(this.endInclusive, range.endInclusive))

// maps cumulatively with the last value
fun <T> Iterable<T>.mapCumulatively(defaultVal: T, mapper: (T, T) -> T): List<T> {
    var last = defaultVal
    return map { item ->
        mapper(last, item).also { last = it }
    }
}

// group lists into a list of list using a predicate check
fun <T> Iterable<T>.groupUntil(until: (T) -> Boolean): List<List<T>> =
    fold(mutableListOf<MutableList<T>>(mutableListOf<T>())){ list, item ->
        if(until(item)) {
            list.add(mutableListOf())
        }else{
            list.last().add(item)
        }
        list
    }

// strip the first integer from a string
fun String.stripInt() =
    stripInts().firstOrNull() ?: 0

// strip all integers from a string
fun String.stripInts(): List<Int> =
    Regex("-?\\d+").findAll(this)
        .asSequence().map {
            it.value.toInt()
        }.toList()

// strip the first long from a string
fun String.stripLong(): Long =
    stripLongs().firstOrNull() ?: 0

// strip all longs from a string
fun String.stripLongs(): List<Long> =
    Regex("-?\\d+").findAll(this)
        .asSequence().map {
            it.value.toLong()
        }.toList()

// inline if check
fun <T> T.computeIf(check: Boolean, compute: (T) -> T) =
    if(check) compute(this)
    else this
