package spectra.interfaces;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpectraContext {
  private final Map<String, Object> data = new ConcurrentHashMap<>();

  public <T> void put(String key, T value) {
    data.put(key, value);
  }

  public void putAll(Map<String, Object> map) {
    this.data.putAll(map);
  }

  <T> void putAll(SpectraContext context) {
    this.data.putAll(context.data);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(String key, Class<T> type) {
    return (T) data.get(key);
  }

  public boolean contains(String key) {
    return data.containsKey(key);
  }

  static final class Holder {
    private static final ThreadLocal<SpectraContext> CURRENT = new ThreadLocal<>();

    public static void put(SpectraContext context) {
      var current = CURRENT.get();
      if (current != null) {
        current.putAll(context);
        return;
      }
      CURRENT.set(context);
    }

    public static SpectraContext current() {
      return CURRENT.get();
    }

    public static void clear() {
      CURRENT.remove();
    }
  }
}
