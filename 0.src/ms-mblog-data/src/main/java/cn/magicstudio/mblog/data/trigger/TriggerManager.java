package cn.magicstudio.mblog.data.trigger;

import cn.wonhigh.retail.backend.core.SpringContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;

public class TriggerManager implements InitializingBean {
	static Map<String, DbTrigger> triggers = new HashMap();

	public static void triger(String metadata, Object param) {
		for (Map.Entry<String, DbTrigger> kv : triggers.entrySet()) {
			if (((DbTrigger) kv.getValue()).match(new Object[] { metadata,
					param })) {
				((DbTrigger) kv.getValue()).trigger(param);
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		Map<String, DbTrigger> items = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(SpringContext.getContext(),
						DbTrigger.class, false, false);

		for (Map.Entry<String, DbTrigger> kv : items.entrySet()) {
			DbTrigger item = (DbTrigger) kv.getValue();
			String typeName = item.getName();
			triggers.put(typeName.toLowerCase(), item);
		}
	}
}