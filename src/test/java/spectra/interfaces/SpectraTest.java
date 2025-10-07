package spectra.interfaces;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import spectrumTest.Bands;
import spectrumTest.BaseSpectrum;
import spectrumTest.CustomSpectrum;
import spectrumTest.DerivedSpectrum;
import spectrumTest.HookedSpectrum;
import spectrumTest.InvalidCooldown;
import spectrumTest.InvalidWarmup;
import spectrumTest.MultiRaySpectrum;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static spectra.interfaces.Spectra.*;

public class SpectraTest {
  @Test
  void givenSunriseAndSunsetHooks_whenReveal_thenHooksAreExecuted() throws Throwable {
    AtomicBoolean sunriseCalled = new AtomicBoolean();
    AtomicBoolean sunsetCalled = new AtomicBoolean();

    var tests = Spectra.of()
      .sunrise(() -> sunriseCalled.set(true))
      .sunset(() -> sunsetCalled.set(true))
      .reveal(Bands.SIMPLE);

    execute(tests);
    assertTrue(sunriseCalled.get(), "Sunrise should have run");
    assertTrue(sunsetCalled.get(), "Sunset should have run");
  }

  @Test
  void givenWarmupAndCooldown_whenRayExecuted_thenHooksAreCalledAroundRay() throws Throwable {
    var hookedSpectrum = new AtomicReference<HookedSpectrum>();

    var tests = Spectra.of().whenRevealed(spectrum -> {
      hookedSpectrum.set((HookedSpectrum) spectrum);
    }).reveal(Bands.HOOKED);

    execute(tests);

    var spectrum = hookedSpectrum.get();
    assertTrue(spectrum.warmupCalled);
    assertTrue(spectrum.rayCalled);
    assertTrue(spectrum.cooldownCalled);
  }

  @Test
  void givenMultipleSpectrums_whenRevealByBand_thenOnlyMatchingBandIsExecuted() {
    var tests = Spectra.of().whenRevealed(spectrum -> {
      assertInstanceOf(HookedSpectrum.class, spectrum);
    }).reveal(Bands.HOOKED);
  }

  @Test
  void givenRayWithCustomName_whenReveal_thenDynamicTestUsesCustomName() {
    var tests = Spectra.of().reveal(Bands.CUSTOM);
    assertTrue(tests.stream().allMatch(t -> t.getDisplayName().equals(CustomSpectrum.CUSTOM_RAY_DESCRIPTION)));
  }

  @Test
  void givenInactiveSpectrum_whenReveal_thenNoTestsAreExecuted() {
    var tests = Spectra.of()
      .reveal(Bands.INACTIVE);

    assertTrue(tests.isEmpty());
  }

  @Test
  void givenInactiveSpectrumWithSunriseAndSunset_whenReveal_thenNoTestsAreExecuted() throws Throwable {
    AtomicBoolean sunriseCalled = new AtomicBoolean();
    AtomicBoolean sunsetCalled = new AtomicBoolean();

    var tests = Spectra.of()
      .sunrise(() -> sunriseCalled.set(true))
      .sunset(() -> sunsetCalled.set(true))
      .reveal(Bands.INACTIVE);

    execute(tests);

    assertFalse(sunriseCalled.get(), "Sunrise should not have run");
    assertFalse(sunsetCalled.get(), "Sunset should not have run");

    assertTrue(tests.isEmpty());
  }

  @Test
  void givenDerivedSpectrum_whenReveal_thenFindsInheritedAndOwnRaysAndHooks() {
    Spectra spectra = Spectra.of();

    var rays = spectra.rays(new DerivedSpectrum());
    var rayNames = rays.stream()
      .map(DynamicTest::getDisplayName)
      .collect(Collectors.toList());

    assertTrue(rayNames.contains("baseRay"));
    assertTrue(rayNames.contains("derivedRay"));

    var warmups = spectra.warmups(new DerivedSpectrum());
    var warmupNames = warmups.stream()
      .map(DynamicTest::getDisplayName)
      .collect(Collectors.toList());

    assertTrue(warmupNames.contains("baseWarmup"));
    assertTrue(warmupNames.contains("derivedWarmup"));

    var cooldowns = spectra.cooldowns(new DerivedSpectrum());
    var cooldownNames = cooldowns.stream()
      .map(DynamicTest::getDisplayName)
      .collect(Collectors.toList());

    assertTrue(cooldownNames.contains("baseCooldown"));
    assertTrue(cooldownNames.contains("derivedCooldown"));
  }

  @Test
  void givenRepeatedSpectraLoads_whenUsingCache_thenReusesMethods() {
    annotatedMethods(BaseSpectrum.class, Ray.class).toArray(); // first scan
    annotatedMethods(BaseSpectrum.class, Ray.class).toArray(); // cached call

    assertTrue(
      Spectra.isCached(BaseSpectrum.class),
      "BaseSpectrum should be cached"
    );
  }

  @Test
  void givenSpectrumWithTwoRays_whenReveal_thenWarmupAndCooldownRunForEachRay() throws Throwable {
    // given
    var spectrumReference = new AtomicReference<MultiRaySpectrum>();

    // when
    var tests = Spectra.of().whenRevealed(spec -> {
      spectrumReference.set((MultiRaySpectrum) spec);
    }).reveal(Bands.HOOKS);

    execute(tests);

    // then
    assertEquals(6, spectrumReference.get().counter, "Each ray should trigger warmup+ray+cooldown (3 executions per ray)");
  }

  @Test
  void givenClassWithTwoWarmups_whenScan_thenThrowsException() {
    assertThrows(IllegalStateException.class, () ->
      Spectra.scanAnnotations(InvalidWarmup.class)
    );
  }

  @Test
  void givenClassWithTwoCooldowns_whenScan_thenThrowsException() {
    assertThrows(IllegalStateException.class, () ->
      Spectra.scanAnnotations(InvalidCooldown.class)
    );
  }

  @Test
  void givenClassWithSharedHooks_whenExecute_thenSharedHooksAreExecuted() throws Throwable {
    var tests = Spectra.of().reveal(Bands.SHARED_HOOKS);
    execute(tests);
    assertEquals(5, tests.size(), "Shared hooks should generate 5 tests");
  }

  protected void execute(Collection<DynamicTest> tests) throws Throwable {
    for(DynamicTest test : tests) {
      test.getExecutable().execute();
    }
  }
}
