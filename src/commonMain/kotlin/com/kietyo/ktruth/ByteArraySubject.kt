package com.kietyo.ktruth

class ByteArraySubject(val actual: ByteArray) {
    fun isEqualTo(expected: ByteArray) {
        assertArrayContentEquals(null, expected, actual, ByteArray::size,
            ByteArray::get, ByteArray?::contentToString, ByteArray?::contentEquals)
    }
}