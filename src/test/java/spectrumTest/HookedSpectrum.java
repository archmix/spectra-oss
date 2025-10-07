package spectrumTest;

import spectra.interfaces.Band;
import spectra.interfaces.Cooldown;
import spectra.interfaces.Ray;
import spectra.interfaces.Spectrum;
import spectra.interfaces.SpectrumSpecification;
import spectra.interfaces.Warmup;

@SpectrumSpecification
public class HookedSpectrum extends Spectrum {
  public Band band() { return Bands.HOOKED; }

  public boolean warmupCalled = false;
  public boolean cooldownCalled = false;
  public boolean rayCalled = false;

  @Warmup
  void setup() { warmupCalled = true; }

  @Cooldown
  void teardown() { cooldownCalled = true; }

  @Ray
  void testRay() { rayCalled = true; }
}