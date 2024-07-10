package de.halfbit.logger.sink.memory

import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic

internal class RingArray<T>(bufferSize: Int) : Iterable<T> {

    @Suppress("UNCHECKED_CAST")
    private val data: Array<T?> = arrayOfNulls<Any>(bufferSize) as Array<T?>
    private var tail: Int = -1
    private var size: Int = 0

    private val head: Int
        get() = if (size == data.size) (tail + 1) % size else 0

    fun add(item: T) {
        tail = (tail + 1) % data.size
        data[tail] = item
        if (size < data.size) size++
    }

    operator fun get(index: Int): T =
        when {
            size == 0 || index < 0 || index >= size ->
                throw IndexOutOfBoundsException("$index, size: $size")
            size == data.size ->
                checkNotNull(data[(head + index) % data.size])
            else ->
                checkNotNull(data[index])
        }

    fun toList(): List<T> =
        iterator().asSequence().toList()

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private val index: AtomicInt = atomic(0)

        override fun hasNext(): Boolean =
            index.value < size

        override fun next(): T =
            get(index.getAndAdd(1))
    }
}
