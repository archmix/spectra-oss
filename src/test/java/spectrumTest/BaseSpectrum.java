package spectrumTest;

import lombok.extern.slf4j.Slf4j;
import spectra.interfaces.Band;
import spectra.interfaces.Cooldown;
import spectra.interfaces.Ray;
import spectra.interfaces.Spectrum;
import spectra.interfaces.SpectrumSpecification;
import spectra.interfaces.Warmup;

@SpectrumSpecification
@Slf4j
public class BaseSpectrum extends Spectrum {
  @Ray
  void baseRay() {
    log.info("Base ray");
  }

  @Cooldown
  void baseCooldown() {
    log.info("Base cooldown");
  }

  @Warmup
  void baseWarmup() {
    log.info("Base warmup");
  }

  @Override
  public Band band() {
    return Bands.DERIVED;
  }
}