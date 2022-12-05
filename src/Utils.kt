import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

// converts a list of 2 items to a pair
fun <T> List<T>.toPair(): Pair<T,T> = Pair(get(0), get(1))

// moves every item in a list one index to the left
fun <T> MutableList<T>.rotateLeft(){
    if (isEmpty()) return
    val item0 = get(0)
    for (i in 0 until lastIndex){
        set(i, get(i+1))
    }
    set(lastIndex, item0)
}

// performs a mapping on both elements of a pair
fun <T, R> Pair<T,T>.mapBoth(mappingFunction: (T) -> R): Pair<R,R> {
    return Pair(mappingFunction.invoke(first), mappingFunction.invoke(second))
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
fun intersect(string1: String, string2: String): Set<Char> =
    string1.toCharArray()
        .intersect(string2
            .toCharArray()
            .asIterable()
            .toSet())

// inline print function for debugging
fun <T> List<T>.print(): List<T> = map {
    it.also { println(it) }
}

// do a thing x times
fun repeat(times: Int, thing: (Int) -> Unit) =
    (1..times).forEach(thing)