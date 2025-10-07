package spectrumTest;

import lombok.extern.slf4j.Slf4j;
import spectra.interfaces.Band;
import spectra.interfaces.Ray;
import spectra.interfaces.Spectrum;
import spectra.interfaces.SpectrumSpecification;

@Slf4j
@SpectrumSpecification(value = "Sample spectrum")
public class CustomSpectrum extends Spectrum {
  public static final String CUSTOM_RAY_DESCRIPTION = "should be used this for description";

  @Ray(CUSTOM_RAY_DESCRIPTION)
  void custom() {
    log.info("Sample ray shining...");
  }

  public Band band() {
    return Bands.CUSTOM;
  }
}
