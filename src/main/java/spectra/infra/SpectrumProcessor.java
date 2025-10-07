package spectra.infra;

import compozitor.processor.core.interfaces.Processor;
import compozitor.processor.core.interfaces.ServiceProcessor;
import spectra.interfaces.Spectrum;
import spectra.interfaces.SpectrumSpecification;

import java.util.List;
import java.util.Set;

@Processor
public class SpectrumProcessor extends ServiceProcessor {

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(SpectrumSpecification.class.getName());
  }

  @Override
  protected Iterable<Class<?>> serviceClasses() {
    return List.of(Spectrum.class);
  }
}