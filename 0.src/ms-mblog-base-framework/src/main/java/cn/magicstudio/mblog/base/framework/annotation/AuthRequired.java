package cn.magicstudio.mblog.base.framework.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.magicstudio.mblog.base.framework.enums.AuthLevelEnum;


@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired
{
  AuthLevelEnum value() default AuthLevelEnum.STRICT;
}
