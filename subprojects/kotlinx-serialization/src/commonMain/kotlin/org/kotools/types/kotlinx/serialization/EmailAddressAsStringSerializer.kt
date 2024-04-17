package org.kotools.types.kotlinx.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import org.kotools.types.EmailAddress

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
        val decodedValue: String = decoder.decodeString()
        val address: EmailAddress? = EmailAddress.fromStringOrNull(decodedValue)
        if (address != null) return address
        DeserializationError(deserializer = this, decodedValue)
            .fail()
    }
}
