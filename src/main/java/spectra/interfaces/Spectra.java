package spectra.interfaces;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DynamicTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.*;

@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class Spectra {
  private Runnable sunrising;
  private Runnable sunsetting;
  private Consumer<Spectrum> whenRevealed = spectrum -> {};
  private static List<Spectrum> spectra;
  private final SpectraContext context = new SpectraContext();
  private static final Map<Class<?>, Map<Class<? extends Annotation>, List<Method>>> METHOD_CACHE =
    new ConcurrentHashMap<>();

  public Spectra sunrise(Runnable sunrising) {
    this.sunrising = sunrising;
    return this;
  }

  public Spectra sunset(Runnable sunsetting) {
    this.sunsetting = sunsetting;
    return this;
  }

  public Spectra context(Consumer<SpectraContext> consumer) {
    consumer.accept(context);
    SpectraContext.Holder.put(context);
    return this;
  }

  public Collection<DynamicTest> reveal(Band band) {
    var tests = new ArrayList<DynamicTest>();

    reveal(band, spectrum -> {
      whenRevealed.accept(spectrum);
      var rays = rays(spectrum);
      rays.forEach(ray -> {
        tests.addAll(warmups(spectrum));
        tests.add(ray);
        tests.addAll(cooldowns(spectrum));
      });
    });

    if(tests.isEmpty()) {
      return tests;
    }

    Optional.ofNullable(this.sunrising).ifPresent(sunrising -> tests.add(0, dynamicTest("Sunrising", sunrising::run)));
    Optional.ofNullable(this.sunsetting).ifPresent(sunsetting -> tests.add(dynamicTest("Sunset", sunsetting::run)));

    return tests;
  }

  Spectra whenRevealed(Consumer<Spectrum> whenRevealed) {
    this.whenRevealed = whenRevealed;
    return this;
  }

  void reveal(Band band, Consumer<Spectrum> consumer) {
    spectra().forEach(spectrum ->{
      if(active(spectrum) && spectrum.band().name().equals(band.name())){
        log.info("Revealing spectrum {} for band {}", description(spectrum), band.name());
        consumer.accept(spectrum);
      }
    });
  }

  boolean active(Spectrum spectrum) {
    return spectrum.getClass().getAnnotation(SpectrumSpecification.class).active();
  }

  final String description(Spectrum spectrum) {
    var description = spectrum.getClass().getAnnotation(SpectrumSpecification.class).value();
    return Optional.ofNullable(description).orElse(spectrum.getClass().getName());
  }

  final Collection<DynamicTest> rays(Spectrum spectrum) {
    return annotatedMethods(spectrum.getClass(), Ray.class)
      .map(method -> DynamicTestFactory.ray(method, spectrum))
      .collect(Collectors.toList());
  }

  final Collection<DynamicTest> warmups(Spectrum spectrum) {
    return annotatedMethods(spectrum.getClass(), Warmup.class)
      .map(method -> DynamicTestFactory.warmup(method, spectrum))
      .collect(Collectors.toList());
  }

  final Collection<DynamicTest> cooldowns(Spectrum spectrum) {
    return annotatedMethods(spectrum.getClass(), Cooldown.class)
      .map(method -> DynamicTestFactory.cooldown(method, spectrum))
      .collect(Collectors.toList());
  }

  static Map<Class<? extends Annotation>, List<Method>> scanAnnotations(Class<?> type) {
    Map<Class<? extends Annotation>, List<Method>> annotations = new HashMap<>();
    Class<?> current = type;
    int totalAllowed = 0;

    while (current != null && current != Spectrum.class) {
      for (Method method : current.getDeclaredMethods()) {
        if(method.isAnnotationPresent(Ray.class)) {
          annotations
            .computeIfAbsent(Ray.class, __ -> new ArrayList<>())
            .add(method);
          continue;
        }

        if(method.isAnnotationPresent(Warmup.class)) {
          annotations
            .computeIfAbsent(Warmup.class, __ -> new ArrayList<>())
            .add(0, method);
          continue;
        }

        if(method.isAnnotationPresent(Cooldown.class)) {
          annotations
            .computeIfAbsent(Cooldown.class, __ -> new ArrayList<>())
            .add(method);
        }
      }
      current = current.getSuperclass();
      totalAllowed++;
    }

    validate(current, annotations.getOrDefault(Warmup.class, List.of()), Warmup.class, totalAllowed);
    validate(current, annotations.getOrDefault(Cooldown.class, List.of()), Cooldown.class, totalAllowed);

    return annotations;
  }

  static void validate(Class<?> type, List<Method> methods, Class<? extends Annotation> annotation, int totalAllowed) {
    if(methods.size() > totalAllowed) {
      throw new IllegalStateException(String.format(
        "Class %s defines multiple @%s methods: [%s]",
        type.getName(),
        annotation.getSimpleName(),
        methods.stream()
          .map(Method::getName)
          .collect(Collectors.joining(", "))
      ));
    }
  }

  static Stream<Method> annotatedMethods(Class<?> type, Class<? extends Annotation> annotation) {
    return METHOD_CACHE
      .computeIfAbsent(type, Spectra::scanAnnotations)
      .getOrDefault(annotation, List.of())
      .stream();
  }

  static boolean isCached(Class<?> type) {
    return METHOD_CACHE.containsKey(type);
  }

  private static List<Spectrum> spectra() {
    if (spectra != null) {
      return spectra;
    }
    synchronized (Spectra.class) {
      spectra = ServiceLoader.load(Spectrum.class)
        .stream()
        .map(ServiceLoader.Provider::get)
        .collect(Collectors.toUnmodifiableList());
      log.debug("Loaded {} spectrum from classpath", spectra.size());
    }
    return spectra;
  }
}
