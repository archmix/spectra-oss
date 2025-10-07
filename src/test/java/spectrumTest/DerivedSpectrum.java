package spectrumTest;

import lombok.extern.slf4j.Slf4j;
import spectra.interfaces.Cooldown;
import spectra.interfaces.Ray;
import spectra.interfaces.SpectrumSpecification;
import spectra.interfaces.Warmup;

@SpectrumSpecification
@Slf4j
public class DerivedSpectrum extends BaseSpectrum {
  @Ray
  void derivedRay() {
    log.info("Derived ray");
  }

  @Warmup
  void derivedWarmup() {
    log.info("Derived warmup");
  }

  @Cooldown
  void derivedCooldown() {
    log.info("Derived cooldown");
  }
}
