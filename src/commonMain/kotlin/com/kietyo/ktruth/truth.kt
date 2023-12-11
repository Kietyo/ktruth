package com.kietyo.ktruth

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.fail

fun assertThat(actual: Double) = DoubleSubject(actual)
fun assertThat(actual: Boolean) = BooleanSubject(actual)
fun assertThat(actual: String) = StringSubject(actual)
fun assertThat(actual: ByteArray) = ByteArraySubject(actual)
fun assertThat(actual: LongRange) = LongRangeSubject(actual)
fun <T> assertThat(actual: T?) = AnySubject(actual)
fun <T> assertThat(actual: Collection<T>) = CollectionSubject(actual)

internal fun messagePrefix(message: String?) = if (message == null) "" else "$message. "

@OptIn(ExperimentalContracts::class)
internal fun <T> checkReferenceAndNullEquality(
    typeName: String,
    message: String?,
    expected: T?,
    actual: T?,
    contentToString: T?.() -> String
): Boolean {
    contract {
        returns(false) implies (expected != null && actual != null)
    }

    if (expected === actual) {
        return true
    }
    if (expected == null) {
        fail(messagePrefix(message) + "Expected <null> $typeName, actual <${actual.contentToString()}>.")
    }
    if (actual == null) {
        fail(messagePrefix(message) + "Expected non-null $typeName <${expected.contentToString()}>, actual <null>.")
    }

    return false
}

private fun elementsDifferMessage(typeName: String, index: Int, expectedElement: Any?, actualElement: Any?): String =
    "$typeName elements differ at index $index. Expected element <$expectedElement>, actual element <${actualElement}>."

internal fun <T> assertArrayContentEquals(
    message: String?,
    expected: T?,
    actual: T?,
    size: (T) -> Int,
    get: T.(Int) -> Any?,
    contentToString: T?.() -> String,
    contentEquals: T?.(T?) -> Boolean,
    returnResult: Boolean = false
): Boolean {
    if (expected.contentEquals(actual)) return true

    val typeName = "Array"

    if (checkReferenceAndNullEquality(
            typeName,
            message,
            expected,
            actual,
            contentToString
        )
    ) return true

    val expectedSize = size(expected)
    val actualSize = size(actual)

    if (expectedSize != actualSize) {
        val sizesDifferMessage = "$typeName sizes differ. Expected size is $expectedSize, actual size is $actualSize."
        val toString = "Expected <${expected.contentToString()}>, actual <${actual.contentToString()}>."

        if (returnResult) {
            return false
        } else {
            fail(messagePrefix(message) + sizesDifferMessage + "\n" + toString)
        }
    }

    for (index in 0 until expectedSize) {
        val expectedElement = expected.get(index)
        val actualElement = actual.get(index)

        if (expectedElement != actualElement) {
            val elementsDifferMessage = elementsDifferMessage(typeName, index, expectedElement, actualElement)
            val toString = "Expected <${expected.contentToString()}>, actual <${actual.contentToString()}>."

            fail(messagePrefix(message) + elementsDifferMessage + "\n" + toString)
        }
    }

    return true
}


inline fun <reified T: Throwable> testAssertFails(block: () -> Unit) {
    var failed = false
    try {
        block()
    } catch (throwable: Throwable) {
        failed = true
        assertIs<T>(throwable)
    }

    assertThat(failed).isTrue()
}

fun testAssertFailsWithMessage(expectedErrorMessage: String, block: () -> Unit) {
    try {
        block()
    } catch (assertionError: AssertionError) {
        assertEquals(expectedErrorMessage, assertionError.message,
            """
                Error messages not equivalent.
                Expected:
                $expectedErrorMessage
                Actual:
                ${assertionError.message}
            """.trimIndent())
    }
}