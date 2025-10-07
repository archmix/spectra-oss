package spectrumTest;

import lombok.extern.slf4j.Slf4j;
import spectra.interfaces.Band;
import spectra.interfaces.Ray;
import spectra.interfaces.Spectrum;
import spectra.interfaces.SpectrumSpecification;

@Slf4j
@SpectrumSpecification(value = "Sample spectrum", active = false)
public class InactiveSpectrum extends Spectrum {

  @Ray
  void shine() {
    log.info("Sample ray shining...");
  }

  @Ray
  void emit() {
    log.info("Sample ray emitting...");
  }

  public Band band() {
    return Bands.INACTIVE;
  }
}
