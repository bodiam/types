package org.kotools.types

import kotlin.test.Test

class ZeroCompanionKotlinSampleTest {
    @Test
    fun `PATTERN should pass`(): Unit =
        assertPrintsTrue(ZeroCompanionKotlinSample::pattern)

    @Test
    fun `fromByte(Byte) should pass`(): Unit =
        assertPrintsTrue(ZeroCompanionKotlinSample::fromByte)

    @Test
    fun `fromByteOrNull(Byte) should pass`(): Unit =
        assertPrintsTrue(ZeroCompanionKotlinSample::fromByteOrNull)

    @Test
    fun `fromShort(Short) should pass`(): Unit =
        assertPrintsTrue(ZeroCompanionKotlinSample::fromShort)

    @Test
    fun `fromShortOrNull(Short) should pass`(): Unit =
        assertPrintsTrue(ZeroCompanionKotlinSample::fromShortOrNull)

    @Test
    fun `orNull(Any) should pass`(): Unit =
        assertPrintsTrue(ZeroCompanionKotlinSample::orNull)

    @Test
    fun `orThrow(Any) should pass`(): Unit =
        assertPrintsTrue(ZeroCompanionKotlinSample::orThrow)
}
