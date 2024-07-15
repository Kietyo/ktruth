package com.kietyo.ktruth

import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CollectionSubject<T>(val actual: Collection<T>) {
    fun isEmpty() {
        assertTrue(actual.isEmpty())
    }
    fun isNotEmpty() {
        assertTrue(actual.isNotEmpty())
    }
    fun hasSize(expectedSize: Int) {
        assertEquals(expectedSize, actual.size)
    }

    fun containsExactlyUnordered(vararg elements: T) {
        containsExactlyUnordered(elements.asIterable())
    }
    fun containsExactlyUnordered(elements: Iterable<T>) {
        val actualSet = actual.toSet()
        val expectedSet = elements.toSet()
        assertEquals(expectedSet.size, actualSet.size,
            """
                Expected: $expectedSet
                Actual: $actualSet
            """.trimIndent()
        )
        for (expected in expectedSet) {
            assertTrue(actualSet.contains(expected))
        }
    }

    fun isEqualTo(expected: Collection<T>) {
        assertContentEquals(expected, actual)
    }
}