package kotools.types.collections

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotools.types.SinceKotoolsTypes
import kotools.types.StabilityLevel

// ---------- Builders ----------

/**
 * Creates a [NotEmptyList] starting with a [head] and containing all the
 * elements of the optional [tail].
 */
@SinceKotoolsTypes("3.0", StabilityLevel.Alpha)
public fun <E> notEmptyListOf(head: E, vararg tail: E): NotEmptyList<E> {
    val list: List<E> = tail.toList()
    return NotEmptyList(head, list)
}

/**
 * Returns a [NotEmptyList] containing all the elements of this collection, or
 * throws an [IllegalArgumentException] if this collection is empty.
 */
@SinceKotoolsTypes("1.3", StabilityLevel.Alpha)
@Throws(IllegalArgumentException::class)
public fun <E> Collection<E>.toNotEmptyList(): NotEmptyList<E> {
    require(isNotEmpty()) { "Given collection shouldn't be empty." }
    val list: List<E> = toList()
    val head: E = list.first()
    val tail: List<E> = list.subList(1, list.size)
    return NotEmptyList(head, tail)
}

/**
 * Returns a [NotEmptyList] containing all the elements of this array, or throws
 * an [IllegalArgumentException] if this array is empty.
 */
@SinceKotoolsTypes("1.3", StabilityLevel.Alpha)
@Throws(IllegalArgumentException::class)
public fun <E> Array<E>.toNotEmptyList(): NotEmptyList<E> =
    toList().toNotEmptyList()

/**
 * Returns a [NotEmptyList] containing all the elements of this collection, or
 * returns `null` if this collection is empty.
 */
@SinceKotoolsTypes("1.3", StabilityLevel.Alpha)
public fun <E> Collection<E>.toNotEmptyListOrNull(): NotEmptyList<E>? = try {
    toNotEmptyList()
} catch (_: IllegalArgumentException) {
    null
}

/**
 * Returns a [NotEmptyList] containing all the elements of this array, or
 * returns `null` if this array is empty.
 */
@SinceKotoolsTypes("1.3", StabilityLevel.Alpha)
public fun <E> Array<E>.toNotEmptyListOrNull(): NotEmptyList<E>? =
    toList().toNotEmptyListOrNull()

/**
 * Returns a [NotEmptyList] containing all the elements of this collection, or
 * returns the result of calling the [defaultValue] function if this collection
 * is empty.
 */
@SinceKotoolsTypes("1.3", StabilityLevel.Alpha)
public inline infix fun <E> Collection<E>.toNotEmptyListOrElse(
    defaultValue: (Collection<E>) -> NotEmptyList<E>
): NotEmptyList<E> = toNotEmptyListOrNull() ?: defaultValue(this)

/**
 * Returns a [NotEmptyList] containing all the elements of this array, or
 * returns the result of calling the [defaultValue] function if this array is
 * empty.
 */
@SinceKotoolsTypes("1.3", StabilityLevel.Alpha)
public inline infix fun <E> Array<E>.toNotEmptyListOrElse(
    defaultValue: (Array<E>) -> NotEmptyList<E>
): NotEmptyList<E> = toNotEmptyListOrNull() ?: defaultValue(this)

/**
 * Representation of lists that contain at least one element.
 *
 * @param E The type of elements contained in this list.
 */
@Serializable(NotEmptyListSerializer::class)
@SinceKotoolsTypes("1.3", StabilityLevel.Alpha)
public class NotEmptyList<out E> private constructor(
    private val list: List<E>
) : List<E> by list,
    NotEmptyCollection<E> {
    override val head: E get() = list.first()

    internal constructor(head: E, tail: List<E>) : this(listOf(head) + tail)

    /**
     * Creates a [NotEmptyList] starting with a [head] and containing all the
     * elements of the optional [tail].
     */
    @Deprecated(
        "Use the notEmptyListOf function instead.",
        ReplaceWith(
            "notEmptyListOf<E>(head, *tail)",
            "kotools.types.collections.notEmptyListOf"
        )
    )
    public constructor(head: E, vararg tail: E) : this(head, tail.toList())

    // ---------- Conversions ----------

    override fun toString(): String = list.toString()
}

internal class NotEmptyListSerializer<E>(elementSerializer: KSerializer<E>) :
    KSerializer<NotEmptyList<E>> {
    private val delegate: KSerializer<List<E>> =
        ListSerializer(elementSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: NotEmptyList<E>): Unit =
        delegate.serialize(encoder, value)

    override fun deserialize(decoder: Decoder): NotEmptyList<E> =
        delegate.deserialize(decoder).toNotEmptyList()
}
