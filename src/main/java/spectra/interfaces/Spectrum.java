package spectra.interfaces;

public abstract class Spectrum {
  protected abstract Band band();

  protected final <T> T context(String key, Class<T> type) {
    return SpectraContext.Holder.current().get(key, type);
  }
}
