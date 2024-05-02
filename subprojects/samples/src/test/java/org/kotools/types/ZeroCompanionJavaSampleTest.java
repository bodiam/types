package org.kotools.types;

import org.junit.jupiter.api.Test;

class ZeroCompanionJavaSampleTest {
    @Test
    void fromByte_should_pass() {
        final ZeroCompanionJavaSample sample = new ZeroCompanionJavaSample();
        Assert.printsTrue(sample::fromByte);
    }

    @Test
    void fromByteOrNull_should_pass() {
        final ZeroCompanionJavaSample sample = new ZeroCompanionJavaSample();
        Assert.printsTrue(sample::fromByteOrNull);
    }

    @Test
    void fromShort_should_pass() {
        final ZeroCompanionJavaSample sample = new ZeroCompanionJavaSample();
        Assert.printsTrue(sample::fromShort);
    }

    @Test
    void fromShortOrNull_should_pass() {
        final ZeroCompanionJavaSample sample = new ZeroCompanionJavaSample();
        Assert.printsTrue(sample::fromShortOrNull);
    }

    @Test
    void fromIntOrNull_should_pass() {
        final ZeroCompanionJavaSample sample = new ZeroCompanionJavaSample();
        Assert.printsTrue(sample::fromIntOrNull);
    }
}
