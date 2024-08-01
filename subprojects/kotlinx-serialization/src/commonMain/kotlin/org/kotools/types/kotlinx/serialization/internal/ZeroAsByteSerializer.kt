package org.kotools.types.kotlinx.serialization.internal

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import org.kotools.types.Zero
import org.kotools.types.internal.InvalidZero

@OptIn(ExperimentalKotoolsTypesApi::class)
internal object ZeroAsByteSerializer : KSerializer<Zero> {
    override val descriptor: SerialDescriptor
        get() {
            val serialName: String = serialNameOf<Zero>()
            return PrimitiveSerialDescriptor(serialName, PrimitiveKind.BYTE)
        }

    override fun serialize(encoder: Encoder, value: Zero) {
        val valueAsByte: Byte = value.toByte()
        encoder.encodeByte(valueAsByte)
    }

    override fun deserialize(decoder: Decoder): Zero {
        val decodedValue: Byte = decoder.decodeByte()
        val zero: Zero? = Zero.orNull(decodedValue)
        if (zero != null) return zero
        val message: String = InvalidZero(decodedValue)
            .toString()
        throw SerializationException(message)
    }
}
