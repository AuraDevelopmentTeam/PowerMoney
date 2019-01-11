package dev.aura.powermoney.common.compat.computercraft.peripheral;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark peripheral methods so that they can be easily found!
 *
 * @author BrainStone
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PeripheralMethod {}
