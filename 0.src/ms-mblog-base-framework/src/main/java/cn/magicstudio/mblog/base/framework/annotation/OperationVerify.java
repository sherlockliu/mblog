package cn.magicstudio.mblog.base.framework.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.magicstudio.mblog.base.framework.enums.OperationVerifyEnum;


@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationVerify {
	OperationVerifyEnum value() default OperationVerifyEnum.NONE;

	String operation() default "";
}
