package kotools.types.collection

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.internal.ErrorMessage
import kotools.types.internal.ExperimentalSince
import kotools.types.internal.InternalKotoolsTypesApi
import kotools.types.internal.KotoolsTypesVersion
import kotools.types.internal.Since
import kotools.types.internal.serializationError
import kotools.types.number.StrictlyPositiveInt
import kotools.types.number.toStrictlyPositiveInt
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmSynthetic

/**
 * Creates a [NotEmptyMap] starting with a [head] and containing all the entries
 * of the optional [tail].
 *
 * Here's a simple usage example:
 *
 * ```kotlin
 * val map: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
 * println(map) // {a=1, b=2}
 * ```
 */
@OptIn(InternalKotoolsTypesApi::class)
@Since(KotoolsTypesVersion.V4_0_0)
public fun <K, V> notEmptyMapOf(
    head: Pair<K, V>,
    vararg tail: Pair<K, V>
): NotEmptyMap<K, V> = listOf(head)
    .plus(tail)
    .toMap()
    .toNotEmptyMap()
    .getOrThrow()

/**
 * Returns an encapsulated [NotEmptyMap] containing all the entries of this map,
 * or returns an encapsulated [IllegalArgumentException] if this map is
 * [empty][Map.isEmpty].
 *
 * Here's a simple usage example:
 *
 * ```kotlin
 * var map: Map<Char, Int> = mapOf('a' to 1, 'b' to 2)
 * var result: Result<NotEmptyMap<Char, Int>> = map.toNotEmptyMap()
 * println(result) // Success({a=1, b=2})
 *
 * map = emptyMap()
 * result = map.toNotEmptyMap()
 * println(result) // Failure(IllegalArgumentException)
 * ```
 *
 * Please note that changes made to the original map will not be reflected on
 * the resulting [NotEmptyMap].
 *
 * ```kotlin
 * val original: MutableMap<Char, Int> = mutableMapOf('a' to 1, 'b' to 2)
 * val notEmptyMap: NotEmptyMap<Char, Int> = original.toNotEmptyMap()
 *     .getOrThrow()
 * println(original) // {a=1, b=2}
 * println(notEmptyMap) // {a=1, b=2}
 *
 * original.clear()
 * println(original) // {}
 * println(notEmptyMap) // {a=1, b=2}
 * ```
 */
@OptIn(InternalKotoolsTypesApi::class)
@Since(KotoolsTypesVersion.V4_0_0)
public fun <K, V> Map<K, V>.toNotEmptyMap(): Result<NotEmptyMap<K, V>> =
    runCatching { NotEmptyMap.orThrow(entries) }

/**
 * Represents a map that has at least one entry, with a key of type [K] and a
 * value of type [V].
 *
 * You can use the [notEmptyMapOf] or the [toNotEmptyMap] functions for building
 * this type.
 */
@JvmInline
@OptIn(InternalKotoolsTypesApi::class)
@Serializable(NotEmptyMapSerializer::class)
@Since(KotoolsTypesVersion.V4_0_0)
public value class NotEmptyMap<K, out V> private constructor(
    private val delegate: Map<K, V>
) {
    /** Contains static declarations for the [NotEmptyMap] type. */
    public companion object {
        /**
         * Creates a [NotEmptyMap] containing all the entries of the specified
         * [map], or throws an [IllegalArgumentException] if the [map] is
         * [empty][Map.isEmpty].
         *
         * Here's an example for calling this function from Kotlin code:
         *
         * ```kotlin
         * val map: Map<Char, Int> = mapOf('a' to 1, 'b' to 2)
         * val result: NotEmptyMap<Char, Int> = NotEmptyMap.create(map)
         * println(result) // Success({a=1, b=2})
         * ```
         *
         * Please note that changes made to the original map will not be
         * reflected on the resulting [NotEmptyMap].
         *
         * ```kotlin
         * val original: MutableMap<Char, Int> =
         *     mutableMapOf('a' to 1, 'b' to 2)
         * val notEmptyMap: NotEmptyMap<Char, Int> =
         *     NotEmptyMap.create(original)
         * println(original) // {a=1, b=2}
         * println(notEmptyMap) // {a=1, b=2}
         *
         * original.clear()
         * println(original) // {}
         * println(notEmptyMap) // {a=1, b=2}
         * ```
         *
         * You can use the [NotEmptyMap.Companion.createOrNull] function for
         * returning `null` instead of throwing an exception in case of invalid
         * [map].
         */
        @ExperimentalKotoolsTypesApi
        @ExperimentalSince(KotoolsTypesVersion.Unreleased)
        @JvmSynthetic
        public fun <K, V> create(map: Map<K, V>): NotEmptyMap<K, V> {
            val result: NotEmptyMap<K, V>? = createOrNull(map)
            return requireNotNull(result, ErrorMessage.Companion::emptyMap)
        }

        /**
         * Creates a [NotEmptyMap] containing all the entries of the specified
         * [map], or returns `null` if the [map] is [empty][Map.isEmpty].
         *
         * Here's an example for calling this function from Kotlin code:
         *
         * ```kotlin
         * val map: Map<Char, Int> = mapOf('a' to 1, 'b' to 2)
         * val result: NotEmptyMap<Char, Int>? = NotEmptyMap.createOrNull(map)
         * println(result) // Success({a=1, b=2})
         * ```
         *
         * Please note that changes made to the original map will not be
         * reflected on the resulting [NotEmptyMap].
         *
         * ```kotlin
         * val original: MutableMap<Char, Int> =
         *     mutableMapOf('a' to 1, 'b' to 2)
         * val notEmptyMap: NotEmptyMap<Char, Int>? =
         *     NotEmptyMap.createOrNull(original)
         * println(original) // {a=1, b=2}
         * println(notEmptyMap) // {a=1, b=2}
         *
         * original.clear()
         * println(original) // {}
         * println(notEmptyMap) // {a=1, b=2}
         * ```
         *
         * You can use the [NotEmptyMap.Companion.create] function for throwing
         * an exception instead of returning `null` in case of invalid [map].
         */
        @ExperimentalKotoolsTypesApi
        @ExperimentalSince(KotoolsTypesVersion.Unreleased)
        @JvmSynthetic
        public fun <K, V> createOrNull(map: Map<K, V>): NotEmptyMap<K, V>? {
            if (map.isEmpty()) return null
            val copiedMap: Map<K, V> =
                map.entries.associate(Map.Entry<K, V>::toPair)
            return NotEmptyMap(copiedMap)
        }

        @InternalKotoolsTypesApi
        @JvmSynthetic
        internal fun <K, V> orThrow(
            entries: Set<Map.Entry<K, V>>
        ): NotEmptyMap<K, V> {
            val map: Map<K, V> = entries.associate(Map.Entry<K, V>::toPair)
            val isValid: Boolean = map.isNotEmpty()
            require(isValid, ErrorMessage.Companion::emptyMap)
            return NotEmptyMap(map)
        }
    }

    /**
     * The first entry of this map.
     *
     * Here's a simple usage example:
     *
     * ```kotlin
     * val map: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
     * val head: Pair<Char, Int> = map.head
     * println(head) // (a, 1)
     * ```
     */
    public val head: Pair<K, V>
        get() = delegate.entries.first()
            .toPair()

    /**
     * All entries of this map except [the first one][head].
     *
     * Here's a simple usage example:
     *
     * ```kotlin
     * val map: NotEmptyMap<Char, Int> =
     *     notEmptyMapOf('a' to 1, 'b' to 2, 'c' to 3)
     * val tail: NotEmptyMap<Char, Int>? = map.tail
     * println(tail) // {b=2, c=3}
     * ```
     */
    public val tail: NotEmptyMap<K, V>?
        get() {
            val tail: List<Map.Entry<K, V>> = delegate.entries.drop(1)
            if (tail.isEmpty()) return null
            val entries: Set<Map.Entry<K, V>> = tail.toSet()
            return orThrow(entries)
        }

    /**
     * All entries of this map.
     *
     * Here's a simple usage example:
     *
     * ```kotlin
     * val map: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
     * val entries: NotEmptySet<Map.Entry<Char, Int>> = map.entries
     * println(entries) // [a=1, b=2]
     * ```
     */
    public val entries: NotEmptySet<Map.Entry<K, V>>
        get() = delegate.entries.toNotEmptySet()
            .getOrThrow()

    /**
     * All keys of this map.
     *
     * Here's a simple usage example:
     *
     * ```kotlin
     * val map: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
     * val keys: NotEmptySet<Char> = map.keys
     * println(keys) // [a, b]
     * ```
     */
    public val keys: NotEmptySet<K>
        get() = delegate.keys.toNotEmptySet()
            .getOrThrow()

    /**
     * All values of this map.
     *
     * Here's a simple usage example:
     *
     * ```kotlin
     * val map: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
     * val values: NotEmptyList<Int> = map.values
     * println(values) // [1, 2]
     * ```
     */
    public val values: NotEmptyList<V>
        get() = delegate.values.toNotEmptyList()
            .getOrThrow()

    /**
     * The size of this map.
     *
     * Here's a simple usage example:
     *
     * ```kotlin
     * val map: NotEmptyMap<Char, Int> = notEmptyMapOf('a' to 1, 'b' to 2)
     * val size: StrictlyPositiveInt = map.size
     * println(size) // 2
     * ```
     */
    public val size: StrictlyPositiveInt
        get() = delegate.size.toStrictlyPositiveInt()
            .getOrThrow()

    /**
     * Returns all entries of this map as a [Map] with keys of type [K] and
     * values of type [V].
     *
     * Here's a simple usage example:
     *
     * ```kotlin
     * val notEmptyMap: NotEmptyMap<Char, Int> = notEmptyMapOf(
     *     'a' to 1,
     *     'b' to 2,
     *     'c' to 3
     * )
     * val map: Map<Char, Int> = notEmptyMap.toMap()
     * println(map) // {a=1, b=2, c=3}
     * ```
     */
    public fun toMap(): Map<K, V> = delegate

    /**
     * Returns the string representation of this map.
     *
     * This function should behave like calling the [Any.toString] function on
     * a [Map] with keys of type [K] and values of type [V].
     *
     * Here's a simple usage example:
     *
     * ```kotlin
     * val notEmptyMap: NotEmptyMap<Char, Int> = notEmptyMapOf(
     *     'a' to 1,
     *     'b' to 2,
     *     'c' to 3
     * )
     * println(notEmptyMap) // {a=1, b=2, c=3}
     *
     * val map: Map<Char, Int> = notEmptyMap.toMap()
     * println("$notEmptyMap" == "$map") // true
     * ```
     */
    override fun toString(): String = "$delegate"
}

@InternalKotoolsTypesApi
internal class NotEmptyMapSerializer<K, V>(
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>
) : KSerializer<NotEmptyMap<K, V>> {
    private val delegate: KSerializer<Map<K, V>> by lazy {
        MapSerializer(keySerializer, valueSerializer)
    }

    override val descriptor: SerialDescriptor by lazy(delegate::descriptor)

    override fun serialize(encoder: Encoder, value: NotEmptyMap<K, V>) {
        val map: Map<K, V> = value.toMap()
        encoder.encodeSerializableValue(delegate, map)
    }

    override fun deserialize(decoder: Decoder): NotEmptyMap<K, V> = decoder
        .decodeSerializableValue(delegate)
        .toNotEmptyMap()
        .getOrElse { serializationError(ErrorMessage.emptyMap) }
}
