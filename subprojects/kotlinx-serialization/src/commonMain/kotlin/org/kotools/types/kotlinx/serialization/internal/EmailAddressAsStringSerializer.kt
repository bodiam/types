package org.kotools.types.kotlinx.serialization.internal

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import org.kotools.types.EmailAddress
import org.kotools.types.internal.InvalidEmailAddress

@OptIn(ExperimentalKotoolsTypesApi::class)
internal object EmailAddressAsStringSerializer : KSerializer<EmailAddress> {
    override val descriptor: SerialDescriptor
        get() {
            val serialName: String = serialNameOf<EmailAddress>()
            return PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)
        }

    override fun serialize(encoder: Encoder, value: EmailAddress): Unit = value
        .toString()
        .let(encoder::encodeString)

    override fun deserialize(decoder: Decoder): EmailAddress {
        val text: String = decoder.decodeString()
        val address: EmailAddress? = EmailAddress.fromStringOrNull(text)
        if (address != null) return address
        val exception = InvalidEmailAddress(text, EmailAddress.PATTERN)
        throw SerializationException("$exception")
    }
}
