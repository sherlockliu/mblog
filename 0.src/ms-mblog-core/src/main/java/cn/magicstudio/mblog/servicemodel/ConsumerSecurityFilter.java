package cn.magicstudio.mblog.servicemodel;

import cn.wonhigh.retail.backend.core.ApplicationContext;
import cn.wonhigh.retail.backend.security.Authorization;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.yougou.logistics.base.common.model.SystemUser;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component("consumerFilter")
@Activate(group = { "consumer" })
public class ConsumerSecurityFilter extends BaseDubboFilter implements Filter {
	public ConsumerSecurityFilter() {
		this.type = "C";
	}

	protected void preInvoke(Invoker<?> invoker, Invocation invocation) {
		log.debug("set cusumer security info.");
		try {
			SystemUser user = Authorization.getUser();
			Integer userId = 0;
			if (user != null) {
				userId = user.getUserid();
			}
			Thread thread = Thread.currentThread();
			String threadName = thread.getName();
			String name = (String) ApplicationContext.current()
					.getValue("user");

			RpcContext.getContext().setAttachment("user", name);
			RpcContext.getContext().setAttachment("userId", userId.toString());
			RpcContext.getContext().setAttachment("thread.id", threadName);
			RpcContext.getContext().setAttachment("client",
					ApplicationContext.host);
		} catch (Exception localException) {
		}
	}

	protected void afterInvoke(Result result) {
		clear();
	}

	protected void error(RpcException ex) {
		clear();
		super.error(ex);
	}
}
