package spectrumTest;

import spectra.interfaces.Band;
import spectra.interfaces.Cooldown;
import spectra.interfaces.Ray;
import spectra.interfaces.Spectrum;
import spectra.interfaces.SpectrumSpecification;
import spectra.interfaces.Warmup;

@SpectrumSpecification
public class BaseSharedHooksSpectrum extends Spectrum {
  @Override
  protected Band band() {
    return Bands.SHARED_HOOKS;
  }

  @Warmup
  public void warmup() {
    System.out.println("base warmup");
  }

  @Cooldown
  public void cooldown() {
    System.out.println("base cooldown");
  }
}
