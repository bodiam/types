package org.kotools.types.kotlinx.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import org.kotools.types.internal.Error

internal class DeserializationError(
    private val deserializer: DeserializationStrategy<*>,
    private val decodedValue: Any,
    private val reason: Error? = null
) : Error {
    @OptIn(ExperimentalSerializationApi::class)
    override val message: String
        get() {
            val serialName: String = this.deserializer.descriptor.serialName
            val reasonMessage: String = this.reason?.message ?: ""
            return "Unable to deserialize '$serialName' from ${decodedValue}." +
                    reasonMessage
        }
}
