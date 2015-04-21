//@DECLARE@
package net.casnw.home.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 变量元数据注解
 *
 * @author 敏玉芳 
 * @since 2013-12-01
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD) 
public @interface VariableMeta {
    String name();
    DatatypeEnum dataType();
    String description() default "";
    UnitEnum unit() default UnitEnum.C;
    String range() default "";
    String value() default "";
    int size() default 0;      
}

