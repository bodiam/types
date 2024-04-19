package org.kotools.types

import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ZeroTest {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    @Test
    fun structural_equality_should_pass_with_another_Zero() {
        val first = Zero()
        val second = Zero()
        assertEquals(first, second)
        val firstHashCode: Int = first.hashCode()
        val secondHashCode: Int = second.hashCode()
        assertEquals(firstHashCode, secondHashCode)
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    @Test
    fun structural_equality_should_fail_with_null() {
        val first = Zero()
        val second: Any? = null
        assertNotEquals(second, first)
        val firstHashCode: Int = first.hashCode()
        val secondHashCode: Int = second.hashCode()
        assertNotEquals(secondHashCode, firstHashCode)
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    @Test
    fun structural_equality_should_fail_with_another_type_than_Zero() {
        val first = Zero()
        val second: Any = "oops"
        assertNotEquals(second, first)
        val firstHashCode: Int = first.hashCode()
        val secondHashCode: Int = second.hashCode()
        assertNotEquals(secondHashCode, firstHashCode)
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    @Test
    fun toByte_should_pass() {
        val actual: Byte = Zero()
            .toByte()
        val expected: Byte = 0.toByte()
        assertEquals(expected, actual)
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    @Test
    fun toString_should_pass() {
        val actual: String = Zero()
            .toString()
        val expected = "0"
        assertEquals(expected, actual)
    }
}

class ZeroCompanionTest {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    @Test
    fun fromByteOrNull_should_pass_with_a_Byte_that_equals_zero() {
        val number: Byte = 0
        val actual: Zero? = Zero.fromByteOrNull(number)
        assertNotNull(actual)
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    @Test
    fun fromByteOrNull_should_fail_with_a_Byte_other_than_zero() {
        val number: Byte = listOf(Byte.MIN_VALUE..-1, 1..Byte.MAX_VALUE)
            .random()
            .random()
            .toByte()
        val actual: Zero? = Zero.fromByteOrNull(number)
        assertNull(actual)
    }
}
