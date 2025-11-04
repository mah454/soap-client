package ir.sample.framework;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface SoapField {
    String value();                // local name (e.g., "sayHelloResponse")

    String namespace() default ""; // optional namespace URI

    String prefix() default "";    // optional prefix (e.g., "ex")
}
