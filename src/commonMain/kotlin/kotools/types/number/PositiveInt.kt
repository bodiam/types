/*
 * Copyright 2022-2023 Loïc Lamarque and Kotools S.A.S.U.
 * Use of this source code is governed by the MIT license.
 */

package kotools.types.number

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.experimental.ExperimentalRangeApi
import kotools.types.experimental.NotEmptyRange
import kotools.types.experimental.notEmptyRangeOf
import kotools.types.experimental.range
import kotools.types.internal.ExperimentalSince
import kotools.types.internal.KotoolsTypesPackage
import kotools.types.internal.KotoolsTypesVersion
import kotools.types.internal.Since
import kotools.types.internal.intSerializer
import kotools.types.internal.simpleNameOf

/**
 * Returns this number as an encapsulated [PositiveInt], which may involve
 * rounding or truncation, or returns an encapsulated [IllegalArgumentException]
 * if this number is [strictly negative][StrictlyNegativeInt].
 */
@Since(KotoolsTypesVersion.V4_1_0)
public fun Number.toPositiveInt(): Result<PositiveInt> {
    val value: Int = toInt()
    return when {
        value == 0 -> Result.success(ZeroInt)
        value.isStrictlyPositive() -> value.toStrictlyPositiveInt()
        else -> {
            val exception = PositiveIntConstructionException(value)
            Result.failure(exception)
        }
    }
}

/**
 * Represents an integer number of type [Int] that is greater than or equals
 * zero.
 */
@Serializable(PositiveIntSerializer::class)
@Since(KotoolsTypesVersion.V1_1_0)
public sealed interface PositiveInt : AnyInt {
    /** Contains declarations for holding or building a [PositiveInt]. */
    public companion object {
        /** The minimum value a [PositiveInt] can have. */
        public val min: ZeroInt = ZeroInt

        /** The maximum value a [PositiveInt] can have. */
        public val max: StrictlyPositiveInt by lazy { StrictlyPositiveInt.max }

        /** The range of values a [PositiveInt] can have. */
        @ExperimentalRangeApi
        @ExperimentalSince(KotoolsTypesVersion.V4_2_0)
        @OptIn(ExperimentalKotoolsTypesApi::class)
        public val range: NotEmptyRange<PositiveInt> by lazy {
            val end: StrictlyPositiveInt = StrictlyPositiveInt.range.end.value
            notEmptyRangeOf { ZeroInt.inclusive to end.inclusive }
        }

        /** Returns a random [PositiveInt]. */
        @Since(KotoolsTypesVersion.V3_0_0)
        public fun random(): PositiveInt = (min.toInt()..max.toInt())
            .random()
            .toPositiveInt()
            .getOrThrow()
    }

    @Since(KotoolsTypesVersion.V4_0_0)
    override fun toInt(): Int

    @Since(KotoolsTypesVersion.V4_0_0)
    override fun toString(): String
}

/**
 * Divides this integer by the [other] one, truncating the result to an integer
 * that is closer to [zero][ZeroInt].
 */
@Since(KotoolsTypesVersion.V4_1_0)
public operator fun PositiveInt.div(other: StrictlyPositiveInt): PositiveInt {
    val result: Int = toInt() / other
    return result.toPositiveInt()
        .getOrThrow()
}

/**
 * Divides this integer by the [other] one, truncating the result to an integer
 * that is closer to [zero][ZeroInt].
 */
@Since(KotoolsTypesVersion.V4_1_0)
public operator fun PositiveInt.div(other: StrictlyNegativeInt): NegativeInt {
    val result: Int = toInt() / other
    return result.toNegativeInt()
        .getOrThrow()
}

/**
 * Calculates the remainder of truncating division of this integer by the
 * [other] one.
 */
@Since(KotoolsTypesVersion.V4_1_0)
public operator fun PositiveInt.rem(other: NonZeroInt): PositiveInt {
    val result: Int = toInt() % other
    return result.toPositiveInt()
        .getOrThrow()
}

private object PositiveIntSerializer :
    KSerializer<PositiveInt> by intSerializer(
        PositiveIntDeserializationStrategy,
        intConverter = { it.toInt() }
    )

private object PositiveIntDeserializationStrategy :
    DeserializationStrategy<PositiveInt> {
    override val descriptor: SerialDescriptor by lazy {
        val simpleName: String = simpleNameOf<PositiveInt>()
        val serialName = "${KotoolsTypesPackage.Number}.$simpleName"
        PrimitiveSerialDescriptor(serialName, PrimitiveKind.INT)
    }

    override fun deserialize(decoder: Decoder): PositiveInt {
        val value: Int = decoder.decodeInt()
        return value.toPositiveInt()
            .getOrNull()
            ?: throw PositiveIntSerializationException(value)
    }
}

internal class PositiveIntConstructionException(number: Number) :
    IllegalArgumentException() {
    override val message: String by lazy {
        "Number should be positive (tried with $number)."
    }
}

private class PositiveIntSerializationException(number: Number) :
    SerializationException() {
    override val message: String by lazy {
        "Number should be positive (tried with $number)."
    }
}
