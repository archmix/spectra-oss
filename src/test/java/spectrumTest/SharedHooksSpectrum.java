package spectrumTest;

import spectra.interfaces.Cooldown;
import spectra.interfaces.Ray;
import spectra.interfaces.SpectrumSpecification;
import spectra.interfaces.Warmup;

@SpectrumSpecification
public class SharedHooksSpectrum extends BaseSharedHooksSpectrum {
  @Warmup
  public void subWarmup() {
    System.out.println("sub warmup");
  }

  @Cooldown
  public void subCooldown() {
    System.out.println("sub cooldown");
  }

  @Ray
  public void baseRay() {
    System.out.println("base ray");
  }
}
