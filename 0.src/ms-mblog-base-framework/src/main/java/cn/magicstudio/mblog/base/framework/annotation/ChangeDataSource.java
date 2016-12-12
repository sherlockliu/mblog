package cn.magicstudio.mblog.base.framework.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeDataSource
{
  String value() default "default";
}