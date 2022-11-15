package kotools.types.collections

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotools.types.*
import kotools.types.string.toNotBlankStringOrThrow

// ---------- Builders ----------

/**
 * Creates a [NotEmptyList] starting with a [head] and containing all the
 * elements of the optional [tail].
 */
@SinceKotoolsTypes("3.0")
public fun <E> notEmptyListOf(head: E, vararg tail: E): NotEmptyList<E> {
    val list: List<E> = tail.toList()
    return notEmptyListOf(head, list)
}

private fun <E> notEmptyListOf(head: E, tail: List<E>): NotEmptyList<E> {
    val list: List<E> = listOf(head) + tail
    return NotEmptyListImplementation(list)
}

// Will replace the Collection.toNotEmptyList function in v3.4.
private fun <E> Collection<E>.toNotEmptyListImplementation():
        KotoolsTypesBuilderResult<NotEmptyList<E>> =
    takeIf(Collection<E>::isNotEmpty)
        ?.toList()
        ?.run { first() to subList(1, size) }
        ?.toSuccessfulResult { notEmptyListOf(it.first, it.second) }
        ?: builderError(
            "Given collection shouldn't be empty."::toNotBlankStringOrThrow
        )

/**
 * Returns a [NotEmptyList] containing all the elements of this collection, or
 * throws an [IllegalArgumentException] if this collection is empty.
 */
@Deprecated(
    "Use the Collection.toNotEmptyListOrThrow function instead. Will be an error in v3.3.",
    ReplaceWith(
        "this.toNotEmptyListOrThrow()",
        "${Package.collections}.toNotEmptyListOrThrow"
    )
)
@SinceKotoolsTypes("1.3")
@Throws(IllegalArgumentException::class)
public fun <E> Collection<E>.toNotEmptyList(): NotEmptyList<E> {
    require(isNotEmpty()) { "Given collection shouldn't be empty." }
    val list: List<E> = toList()
    val head: E = list.first()
    val tail: List<E> = list.subList(1, list.size)
    return notEmptyListOf(head, tail)
}

/**
 * Returns a [NotEmptyList] containing all the elements of this array, or throws
 * an [IllegalArgumentException] if this array is empty.
 */
@SinceKotoolsTypes("1.3")
@Throws(IllegalArgumentException::class)
public fun <E> Array<E>.toNotEmptyList(): NotEmptyList<E> = toList()
    .toNotEmptyListOrThrow()

/**
 * Returns a [NotEmptyList] containing all the elements of this collection, or
 * returns the result of calling the [defaultValue] function if this collection
 * is empty.
 */
@SinceKotoolsTypes("1.3")
public inline infix fun <E> Collection<E>.toNotEmptyListOrElse(
    defaultValue: (Collection<E>) -> NotEmptyList<E>
): NotEmptyList<E> = toNotEmptyListOrNull() ?: defaultValue(this)

/**
 * Returns a [NotEmptyList] containing all the elements of this array, or
 * returns the result of calling the [defaultValue] function if this array is
 * empty.
 */
@SinceKotoolsTypes("1.3")
public inline infix fun <E> Array<E>.toNotEmptyListOrElse(
    defaultValue: (Array<E>) -> NotEmptyList<E>
): NotEmptyList<E> = toNotEmptyListOrNull() ?: defaultValue(this)

/**
 * Returns a [NotEmptyList] containing all the elements of this collection, or
 * returns `null` if this collection is empty.
 */
@SinceKotoolsTypes("1.3")
public fun <E> Collection<E>.toNotEmptyListOrNull(): NotEmptyList<E>? =
    tryOrNull(::toNotEmptyListOrThrow)

/**
 * Returns a [NotEmptyList] containing all the elements of this array, or
 * returns `null` if this array is empty.
 */
@SinceKotoolsTypes("1.3")
public fun <E> Array<E>.toNotEmptyListOrNull(): NotEmptyList<E>? = toList()
    .toNotEmptyListOrNull()

/**
 * Returns a [NotEmptyList] containing all the elements of this collection, or
 * throws an [IllegalArgumentException] if this collection is empty.
 */
@SinceKotoolsTypes("3.2")
@Throws(IllegalArgumentException::class)
public fun <E> Collection<E>.toNotEmptyListOrThrow(): NotEmptyList<E> =
    toNotEmptyListImplementation()
        .onError { throw it }

/**
 * Representation of lists that contain at least one element.
 *
 * @param E The type of elements contained in this list.
 */
@Serializable(NotEmptyListSerializer::class)
@SinceKotoolsTypes("1.3")
public sealed interface NotEmptyList<out E> : NotEmptyCollection<E>, List<E>

private class NotEmptyListImplementation<out E>(private val list: List<E>) :
    NotEmptyList<E>,
    List<E> by list {
    override val head: E get() = list.first()

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

    override fun deserialize(decoder: Decoder): NotEmptyList<E> = delegate
        .deserialize(decoder)
        .toNotEmptyListOrThrow()
}
