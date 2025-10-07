package spectra.application;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DynamicTest;
import spectra.interfaces.Band;
import spectra.interfaces.Ray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.DynamicTest.*;

@RequiredArgsConstructor(staticName = "of")
public class Spectrum {
  private final Band band;
  private Runnable sunrising;
  private Runnable sunsetting;
  private Runnable warmUp = () -> {};
  private Runnable coolDown = () -> {};

  public Spectrum sunrise(Runnable sunrising) {
    this.sunrising = sunrising;
    return this;
  }

  public Spectrum sunset(Runnable sunsetting) {
    this.sunsetting = sunsetting;
    return this;
  }

  public Spectrum warmUp(Runnable warmUp) {
    this.warmUp = warmUp;
    return this;
  }

  public Spectrum coolDown(Runnable coolDown) {
    this.coolDown = coolDown;
    return this;
  }

  public Collection<DynamicTest> beams() {
    Collection<DynamicTest> tests = new ArrayList<>();
    if (this.sunrising != null) {
      tests.add(dynamicTest("Sunrise", () -> this.sunrising.run()));
    }

    for(Ray ray : ServiceLoader.load(Ray.class)) {
      if(!ray.band().name().equals(this.band.name())) {
        continue;
      }
      tests.add(dynamicTest(ray.description(), () -> {
        Spectrum.this.warmUp.run();
        ray.shine();
        Spectrum.this.coolDown.run();
      }));
    }

    if (this.sunsetting != null) {
      tests.add(dynamicTest("Sunset", () -> this.sunsetting.run()));
    }
    return tests;
  }
}
