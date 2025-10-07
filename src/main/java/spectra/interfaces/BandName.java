package spectra.interfaces;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
class BandName implements Band {
  private final String name;

  @Override
  public String name() {
    return this.name;
  }
}
