package cn.magicstudio.mblog.base.framework.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.magicstudio.mblog.base.framework.enums.DataAccessRuleEnum;


@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataAccessAuth
{
  DataAccessRuleEnum[] value() default {};
}
