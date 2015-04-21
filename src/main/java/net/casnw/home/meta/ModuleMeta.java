//@DECLARE@
package net.casnw.home.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块注解类
 *
 * @author 敏玉芳
 * @since 2013-12-01
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ModuleMeta {

    String name() default "";

    String author() default "";

    String version() default "";

    String keyword() default "";

    String description() default "";

    String category() default "";

    String applicationField() default "";

    String theory() default "";

    SpacescaleEnum spaceScale() default SpacescaleEnum.GRID;

    TimescaleEnum timeScale() default TimescaleEnum.YEAR;

    SparefsysEnum spaRefSys() default SparefsysEnum.BASIN;

    //所属模型
    String model() default "";
}
