package com.kietyo.ktruth

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class AnySubject<T : Any?>(val actual: T?) {
    fun isEqualTo(expected: T) {
        assertEquals(expected, actual)
    }

    inline fun <reified E : Any> isInstanceOf(): E {
        return isInstanceOf<E>(actual)
    }

    @OptIn(ExperimentalContracts::class)
    inline fun <reified E : Any> isInstanceOf(value: Any?): E {
        contract { returns() implies (value is E) }
        assertIs<E>(actual)
        return actual
    }

    fun isNull() {
        assertNull(actual)
    }
}