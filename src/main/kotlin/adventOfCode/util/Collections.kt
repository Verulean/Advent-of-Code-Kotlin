package adventOfCode.util

fun <T> Collection<T>.pairs(): Sequence<PairOf<T>> = this.zipWithNext().asSequence() + sequenceOf(this.last() to this.first())

fun <T> Collection<T>.toPair() = this.zipWithNext().single()

fun <T> Collection<T>.toTriple(): TripleOf<T> {
    assert(this.size == 3)
    val (x, y, z) = this.take(3)
    return Triple(x, y, z)
}

fun <T> MutableSet<T>.pop(): T {
    val ret = this.first()
    this.remove(ret)
    return ret
}

fun<T> Collection<T>.combinations(r: Int): Sequence<List<T>> {
    val elements = this.toList()
    val n = this.size
    return sequence {
        yield(elements.take(r))
        val indices = (0..<r).toMutableList()
        while (true) {
            val i = indices.withIndex().reversed().firstOrNull { (i, j) -> j != i + n - r }?.index ?: return@sequence
            indices[i]++
            (i + 1..<r).forEach { indices[it] = indices[it - 1] + 1 }
            yield(indices.map(elements::get))
        }
    }
}

open class Counter<E>(collection: Iterable<E> = emptyList()) : DefaultHashMap<E, Int>({ 0 }) {
    init { collection.forEach { this[it]++ } }
}

open class DefaultHashMap<K, V>(val factory: () -> V) : HashMap<K, V>() {
    override operator fun get(key: K): V {
        if (!super.containsKey(key)) set(key, factory())
        return super.get(key)!!
    }
}
