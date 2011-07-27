package de.ckl.testing.handler.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker anntotation that given field handler can generate unique data. A
 * BooleanHandler should not be annotated by this class...
 * 
 * @author ckl
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CreatesUniqueValues {
}
