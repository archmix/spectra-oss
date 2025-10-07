package spectra.interfaces;

import java.util.ServiceLoader;
import java.util.function.Consumer;

public interface Band {
  String name();

  static Band of(String name){
    return BandName.of(name);
  }

  Band DEFAULT = Band.of("default");

}
