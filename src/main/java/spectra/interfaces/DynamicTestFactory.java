package spectra.interfaces;

import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Method;

class DynamicTestFactory {
  static DynamicTest ray(Method method, Object instance) {
    var description = method.getAnnotation(Ray.class).value();
    if(description.isEmpty()){
      description = method.getName();
    }
    return DynamicTest.dynamicTest(description, RunnableMethodAdapter.of(method, instance)::run);
  }

  static DynamicTest warmup(Method method, Object instance) {
    var description = method.getAnnotation(Warmup.class).value();
    if(description.isEmpty()){
      description = method.getName();
    }
    return DynamicTest.dynamicTest(description, RunnableMethodAdapter.of(method, instance)::run);
  }

  static DynamicTest cooldown(Method method, Object instance) {
    var description = method.getAnnotation(Cooldown.class).value();
    if(description.isEmpty()){
      description = method.getName();
    }
    return DynamicTest.dynamicTest(description, RunnableMethodAdapter.of(method, instance)::run);
  }
}
