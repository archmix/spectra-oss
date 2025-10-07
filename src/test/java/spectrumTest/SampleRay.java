package spectrumTest;

import lombok.extern.slf4j.Slf4j;
import spectra.interfaces.Band;
import spectra.interfaces.Ray;
import spectra.interfaces.RaySpecification;

@RaySpecification(description = "Sample Specification")
@Slf4j
public class SampleRay implements Ray {

  @Override
  public void shine() {
    log.info("Sample ray shining...");
  }

  public Band band() {
    return Bands.SAMPLE;
  }
}
