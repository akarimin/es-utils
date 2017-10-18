package ir.hafiz.esutils.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by akarimin on 9/6/17.
 */

/***********************************************************************************************************************
 *  In order to avoid boilerplate code while using Builder pattern, take GenericBuilder for granted.
 *  e.g.:
 *   Person value = GenericBuilder.of(Person::new).with(Person::setName, "Ali").with(Person::setAge, 35).build();
 **********************************************************************************************************************/
public class GenericBuilder<T> {

  private final Supplier<T> instantiator;

  private List<Consumer<T>> instanceModifiers = new ArrayList<>();

  public GenericBuilder(Supplier<T> instantiator) {
    this.instantiator = instantiator;
  }

  public static <T> GenericBuilder<T> of(Supplier<T> instantiator) {
    return new GenericBuilder<T>(instantiator);
  }

  public <U> GenericBuilder<T> with(BiConsumer<T, U> consumer, U value) {
    Consumer<T> c = instance -> consumer.accept(instance, value);
    instanceModifiers.add(c);
    return this;
  }

  public T build() {
    T value = instantiator.get();
    instanceModifiers.forEach(modifier -> modifier.accept(value));
    instanceModifiers.clear();
    return value;
  }
}
