package spectraContextTest;

import org.junit.jupiter.api.*;
import spectra.interfaces.Band;
import spectra.interfaces.Ray;
import spectra.interfaces.Spectra;
import spectra.interfaces.SpectraContext;
import spectra.interfaces.Spectrum;
import spectrumTest.Bands;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class SpectraContextTest {

  @Test
  void givenContext_whenPutAndGet_thenValueIsAccessible() {
    // given
    SpectraContext context = new SpectraContext();
    context.put("user", "Alice");

    // when
    String user = context.get("user", String.class);

    // then
    assertEquals("Alice", user);
    assertTrue(context.contains("user"));
  }

  @Test
  void givenSpectraWithContext_whenReveal_thenSpectrumCanAccessContext() throws Throwable {
    var key = "config";
    var value = "test-mode";
    AtomicBoolean contextAccessed = new AtomicBoolean(false);

    // given
    Spectra spectra = Spectra.of().context(context ->{
      context.put(key, value);
    });

    // mock Spectrum
    Spectrum testSpectrum = new Spectrum() {
      @Override
      public Band band() {
        return Bands.SIMPLE;
      }

      @Ray
      public void contextRay() {
        String config = context(key, String.class);
        assertEquals(value, config, "Context value should be available in Ray");
        contextAccessed.set(true);
      }
    };

    Collection<DynamicTest> tests = List.of(
      dynamicTest("contextRay", () -> testSpectrum.getClass()
        .getDeclaredMethod("contextRay").invoke(testSpectrum))
    );

    // then
    for(DynamicTest test : tests) {
      test.getExecutable().execute();
    }

    assertTrue(contextAccessed.get(), "Context should be accessible in Ray");
  }
}
