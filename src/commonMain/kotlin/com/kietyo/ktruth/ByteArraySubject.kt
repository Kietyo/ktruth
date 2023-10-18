package com.kietyo.ktruth

import kotlin.test.assertFalse

class ByteArraySubject(val actual: ByteArray) {
    fun isEqualTo(expected: ByteArray) {
        assertArrayContentEquals(null, expected, actual, ByteArray::size,
            ByteArray::get, ByteArray?::contentToString, ByteArray?::contentEquals)
    }

    fun isNotEqualTo(expected: ByteArray) {
        assertFalse {
            assertArrayContentEquals(null, expected, actual, ByteArray::size,
                ByteArray::get, ByteArray?::contentToString, ByteArray?::contentEquals, true)
        }
    }
}