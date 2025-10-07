package spectrumTest;

import lombok.extern.slf4j.Slf4j;
import spectra.interfaces.Band;
import spectra.interfaces.Cooldown;
import spectra.interfaces.Spectrum;
import spectra.interfaces.Warmup;

@Slf4j
public class InvalidCooldown extends Spectrum {
  @Cooldown
  public void cooldown() {
    log.info("this is allowed");
  }

  @Cooldown
  public void invalidCooldown() {
    log.info("this is not allowed");
  }

  @Override
  protected Band band() {
    return Bands.INVALID_COOLDOWN;
  }
}
