package cn.magicstudio.mblog.servicemodel;

import cn.wonhigh.retail.backend.core.ApplicationContext;
import cn.wonhigh.retail.backend.monitor.PerformanceLogger;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDubboFilter implements Filter {
	protected static final Logger log = LoggerFactory
			.getLogger(BaseDubboFilter.class);

	protected String type;

	protected abstract void preInvoke(Invoker<?> paramInvoker,
			Invocation paramInvocation);

	protected abstract void afterInvoke(Result paramResult);

	protected void error(RpcException ex) {
	}

	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {
		long beginTime = System.currentTimeMillis();
		RpcContext context = RpcContext.getContext();

		String url = invoker.getUrl().toString().split("\\?")[0] + "?"
				+ invocation.getMethodName();

		try {
			preInvoke(invoker, invocation);

			Result result = invoker.invoke(invocation);

			String tag = "";
			if (this.type.equals("S")) {
				tag = ApplicationContext.current().getValue("client") + "$"
						+ ApplicationContext.current().getValue("user");
			}

			afterInvoke(result);

			if (url.indexOf("monitor") < 0) {
				long take = System.currentTimeMillis() - beginTime;
				PerformanceLogger.log(this.type, url, "ok", take, tag);
			}
			return result;
		} catch (RpcException e) {
			long take = System.currentTimeMillis() - beginTime;

			PerformanceLogger.log(this.type, url, "error", take);
			error(e);
			throw e;
		}
	}

	protected void clear() {
		try {
			RpcContext.getContext().removeAttachment("user");
			RpcContext.getContext().removeAttachment("thread.id");
			RpcContext.getContext().removeAttachment("client");
		} catch (Exception e) {
			log.info("clearn dubbo context info error.", e);
		}
	}
}
