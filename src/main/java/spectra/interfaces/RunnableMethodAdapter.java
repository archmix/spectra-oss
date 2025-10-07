package spectra.interfaces;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@RequiredArgsConstructor(staticName = "of")
class RunnableMethodAdapter implements Runnable {
  private final Method method;
  private final Object instance;

  @Override
  public void run() {
    try {
      method.setAccessible(true);
      method.invoke(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
