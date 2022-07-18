package kotools.types.collections

import kotools.types.annotations.SinceKotoolsTypes

// ---------- Conversions ----------

/**
 * Returns a not empty set containing all the elements of this array, or throws
 * an [IllegalArgumentException] if this array is empty.
 */
@SinceKotoolsTypes("1.3")
@Throws(IllegalArgumentException::class)
public inline fun <reified E> Array<E>.toNotEmptySet(): NotEmptySet<E> =
    toList().toNotEmptySet()

/**
 * Returns a not empty set containing all the elements of this collection, or
 * throws an [IllegalArgumentException] if this collection is empty.
 */
@SinceKotoolsTypes("1.3")
@Throws(IllegalArgumentException::class)
public inline fun <reified E> Collection<E>.toNotEmptySet(): NotEmptySet<E> {
    require(isNotEmpty()) { "Given collection shouldn't be empty." }
    val head: E = first()
    val set: MutableSet<E> = mutableSetOf()
    for (index in 1 until size) set += elementAt(index)
    val tail: Array<E> = set.toTypedArray()
    return NotEmptySet(head, *tail)
}

/**
 * Returns a not empty set containing all the elements of this array, or returns
 * `null` if this array is empty.
 */
@SinceKotoolsTypes("1.3")
public inline fun <reified E> Array<E>.toNotEmptySetOrNull(): NotEmptySet<E>? =
    toList().toNotEmptySetOrNull()

/**
 * Returns a not empty set containing all the elements of this collection, or
 * returns `null` if this collection is empty.
 */
@SinceKotoolsTypes("1.3")
public inline fun <reified E> Collection<E>.toNotEmptySetOrNull(): NotEmptySet<E>? =
    try {
        toNotEmptySet()
    } catch (_: IllegalArgumentException) {
        null
    }

/**
 * Returns a not empty set containing all the elements of this array, or returns
 * the result of calling the [defaultValue] function if this array is empty.
 */
@SinceKotoolsTypes("1.3")
public inline infix fun <reified E> Array<E>.toNotEmptySetOrElse(
    defaultValue: (Array<E>) -> NotEmptySet<E>
): NotEmptySet<E> = toNotEmptySetOrNull() ?: defaultValue(this)

/**
 * Returns a not empty set containing all the elements of this collection, or
 * returns the result of calling the [defaultValue] function if this collection
 * is empty.
 */
@SinceKotoolsTypes("1.3")
public inline infix fun <reified E> Collection<E>.toNotEmptySetOrElse(
    defaultValue: (Collection<E>) -> NotEmptySet<E>
): NotEmptySet<E> = toNotEmptySetOrNull() ?: defaultValue(this)

/**
 * Represents sets that can't be empty.
 *
 * @constructor Creates a not empty set starting with a [head] and containing
 * all the elements of the optional [tail].
 */
@SinceKotoolsTypes("1.3")
public class NotEmptySet<out E>(
    /** First element of this set. */
    public val head: E,
    vararg tail: E
) : AbstractSet<E>() {
    private val tail: Set<E>

    init {
        this.tail = tail.filterNot { it == head }.toSet()
    }

    // ---------- Query operations ----------

    override val size: Int get() = tail.size + 1

    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<E> = mutableSetOf(head).run {
        this += tail
        toSet().iterator()
    }
}
