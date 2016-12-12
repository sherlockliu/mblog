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
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("providerFilter")
@Activate(group = { "provider" })
public class ProviderSecurityFilter extends BaseDubboFilter implements Filter {
	private static String hostAddress = "";

	static {
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
			String[] v = hostAddress.split("\\.");
			hostAddress = v[2] + "." + v[3];
		} catch (UnknownHostException localUnknownHostException) {
		}
	}

	public ProviderSecurityFilter() {
		this.type = "S";
	}

	private void loadUser(String userId) {
		if (StringUtils.isEmpty(userId))
			return;
		SystemUser user = Authorization.getUser(Integer.valueOf(Integer
				.parseInt(userId)));
		Authorization.setUser(user);
	}

	protected void preInvoke(Invoker<?> invoker, Invocation invocation) {
		try {
			String user = RpcContext.getContext().getAttachment("user");
			String threadId = RpcContext.getContext()
					.getAttachment("thread.id");
			String client = RpcContext.getContext().getAttachment("client");
			if (StringUtils.isEmpty(client)) {
				String[] v = RpcContext.getContext().getRemoteHost()
						.split("\\.");
				client = v[2] + "." + v[3];
			}
			ApplicationContext.current().setValue("user", user);
			ApplicationContext.current().setValue("thread.id", threadId);
			ApplicationContext.current().setValue("client", client);
			String threadname = Thread.currentThread().getName();
			String[] tmp = threadname.split("\\$");
			String name = tmp[0] + "$" + client + "$" + user;

			Thread.currentThread().setName(name);
		} catch (Exception localException) {
		}
	}

	protected void afterInvoke(Result result) {
		clear();
	}

	protected void error(RpcException ex) {
		clear();
		RpcException tt = new RpcException(ex.getCode(), hostAddress + ":"
				+ ex.getMessage(), ex.getCause());
		super.error(tt);
	}

	protected void clear() {
		String threadname = Thread.currentThread().getName();
		String[] tmp = threadname.split("\\$");
		Thread.currentThread().setName(tmp[0]);
		super.clear();
	}
}
