package ru.devinside.drm.fairplay.ksm.secret;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.devinside.util.Hexler;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DFunctionTest {
    private TestVector testVector;

    public DFunctionTest(TestVector testVector) {
        this.testVector = testVector;
    }

    @Parameterized.Parameters
    public static Collection<TestVector> params() {
        return Arrays.asList(
                new TestVector(
                        "A4C411C54D94727143504AECE5613DA8C6EE6DD2",
                        "000102030405060708090A0B0C0D0E0F"
                )
        );
    }

    @Test
    public void verify() {
        assertEquals(
                testVector.hash,
                Hexler.toHexString(StubDFunction.STUB.computeHash(Hexler.toByteArray(testVector.r2)))
        );
    }

    private static class TestVector {
        private final String r2;
        private final String hash;

        private TestVector(String r2, String hash) {
            this.r2 = r2;
            this.hash = hash;
        }
    }
}
