package spectrumTest;

import lombok.extern.slf4j.Slf4j;
import spectra.interfaces.Band;
import spectra.interfaces.Cooldown;
import spectra.interfaces.Ray;
import spectra.interfaces.Spectrum;
import spectra.interfaces.SpectrumSpecification;
import spectra.interfaces.Warmup;

@SpectrumSpecification
public class MultiRaySpectrum extends Spectrum {
  public int counter = 0;

  @Warmup
  void warmup() {
    counter++;
  }

  @Ray
  void firstRay() {
    counter++;
  }

  @Ray
  void secondRay() {
    counter++;
  }

  @Cooldown
  void cooldown() {
    counter++;
  }

  @Override
  public Band band() {
    return Bands.HOOKS;
  }
}
