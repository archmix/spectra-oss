package spectra.interfaces;

public interface Ray {
  Band band();

  default String description() {
    return this.getClass().getAnnotation(RaySpecification.class).description();
  }

  void shine() throws Throwable;
}
