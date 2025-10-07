package spectrumTest;

import lombok.extern.slf4j.Slf4j;
import spectra.interfaces.Band;
import spectra.interfaces.Spectrum;
import spectra.interfaces.Warmup;

@Slf4j
public class InvalidWarmup extends Spectrum {
  @Warmup
  public void warmup() {
    log.info("this is allowed");
  }

  @Warmup
  public void invalidWarmup() {
    log.info("this is not allowed");
  }

  @Override
  protected Band band() {
    return Bands.INVALID_WARMUP;
  }
}
