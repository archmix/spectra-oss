package spectrumTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spectra.application.Spectrum;

import static spectra.Runnables.*;
import static org.mockito.Mockito.*;

public class SpectrumTest {
  @Test
  void givenSpectrum_whenBeams_thenReturns() {
    var beams = spectrum().beams();

    Assertions.assertEquals(1, beams.size());
  }

  @Test
  void givenSpectrumWithSunriseSunset_whenBeams_thenReturns() {
    var sunrising = sunrising();
    var sunsetting = sunsetting();

    var beams = spectrum()
      .sunrise(sunrising)
      .sunset(sunsetting)
      .beams();

    execute(beams);

    Assertions.assertEquals(3, beams.size());
    verify(sunrising, times(1)).run();
    verify(sunsetting, times(1)).run();
  }

  @Test
  void givenSpectrumWithCooldownWarmup_whenBeams_thenReturns() {
    Runnable coolingDown = coolingDown();
    Runnable warmingUp = warmingUp();

    var beams = spectrum().warmUp(warmingUp).coolDown(coolingDown).beams();

    execute(beams);

    Assertions.assertEquals(1, beams.size());
    verify(warmingUp, times(1)).run();
    verify(coolingDown, times(1)).run();
  }

  @Test
  void givenFullSpectrum_whenBeams_thenReturns() {
    var sunrising = sunrising();
    var sunsetting = sunsetting();
    Runnable coolingDown = coolingDown();
    Runnable warmingUp = warmingUp();

    var beams = spectrum().sunrise(sunrising).warmUp(warmingUp).coolDown(coolingDown).sunset(sunsetting).beams();

    execute(beams);

    Assertions.assertEquals(3, beams.size());
    verify(warmingUp, times(1)).run();
    verify(coolingDown, times(1)).run();
    verify(sunrising, times(1)).run();
    verify(sunsetting, times(1)).run();
  }

  Spectrum spectrum() {
    return Spectrum.of(Bands.SAMPLE);
  }
}
