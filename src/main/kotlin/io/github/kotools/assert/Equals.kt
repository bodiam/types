package io.github.kotools.assert

import kotlin.test.assertEquals

/** Asserts that the current value is equal to the [other] value. */
public infix fun <T : Any> T.assertEquals(other: T): Unit =
    assertEquals(other) { "$this should be equal to $it" }

/**
 * Asserts that the current value is equal to the [other] value, or compute and
 * report the [lazyMessage]'s result if not.
 */
public inline fun <T : Any> T.assertEquals(
    other: T,
    lazyMessage: T.(T) -> String
): Unit = assertEquals(other, this, lazyMessage(other))

/**
 * Asserts that the current value is equal to the [other] value, or report the
 * [message] if not.
 */
public fun <T : Any> T.assertEquals(other: T, message: String): Unit =
    assertEquals(other, this, message)
