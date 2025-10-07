package spectra;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DynamicTest;
import org.mockito.Mockito;

import java.util.Collection;

public class Runnables {

  public static Runnable coolingDown() {
    return Mockito.spy(new CoolingDown());
  }

  public static Runnable warmingUp() {
    return Mockito.spy(new WarmingUp());
  }

  public static Runnable sunrising() {
    return Mockito.spy(new Sunrising());
  }

  public static Runnable sunsetting() {
    return Mockito.spy(new Sunsetting());
  }

  public static void execute(Collection<DynamicTest> beams) {
    for (DynamicTest beam : beams) {
      try {
        beam.getExecutable().execute();
      } catch (Throwable throwable) {
        throw new RuntimeException(throwable);
      }
    }
  }

  @Slf4j
  static class CoolingDown implements Runnable {
    @Override
    public void run() {
      log.info("Cooling down...");
    }
  }

  @Slf4j
  static class WarmingUp implements Runnable {
    @Override
    public void run() {
      log.info("Warming up...");
    }
  }

  @Slf4j
  static class Sunsetting implements Runnable {
    @Override
    public void run() {
      log.info("Sunsetting...");
    }
  }

  @Slf4j
  static class Sunrising implements Runnable {
    @Override
    public void run() {
      log.info("Sunrising...");
    }
  }
}
