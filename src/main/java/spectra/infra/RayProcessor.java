package spectra.infra;

import compozitor.processor.core.interfaces.Processor;
import compozitor.processor.core.interfaces.ServiceProcessor;
import spectra.interfaces.Ray;
import spectra.interfaces.RaySpecification;

import java.util.List;
import java.util.Set;

@Processor
public class RayProcessor extends ServiceProcessor {

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(RaySpecification.class.getName());
  }

  @Override
  protected Iterable<Class<?>> serviceClasses() {
    return List.of(Ray.class);
  }
}