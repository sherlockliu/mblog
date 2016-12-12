package cn.magicstudio.mblog.base.framework.Interceptor;

import java.lang.reflect.Method;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import cn.magicstudio.mblog.base.framework.annotation.ChangeDataSource;
import cn.magicstudio.mblog.base.framework.utils.DataSourceSwitch;

public class DataSourceAdvice implements MethodInterceptor {
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ChangeDataSource ds = (ChangeDataSource) invocation.getMethod()
				.getAnnotation(ChangeDataSource.class);
		ds = ds == null ? (ChangeDataSource) invocation.getClass()
				.getAnnotation(ChangeDataSource.class) : ds;
		if (ds != null)
			DataSourceSwitch.setCurrentDataSource(ds.value());
		try {
			return invocation.proceed();
		} finally {
			if (ds != null) {
				DataSourceSwitch.setCurrentDataSource(null);
			}
		}
	}
}
