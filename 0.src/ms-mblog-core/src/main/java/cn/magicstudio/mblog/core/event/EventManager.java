package cn.magicstudio.mblog.core.event;

import com.google.common.eventbus.EventBus;
import com.yougou.logistics.base.common.event.IEvent;
import com.yougou.logistics.base.common.event.IEventHandler;
import com.yougou.logistics.base.common.event.disruptor.BelleEventService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

public class EventManager {
	private static EventManager instance = null;

	@Resource
	private BelleEventService belleEventService;

	private static final Map<String, EventBus> subscribers = new HashMap();

	public EventManager() {
		if (instance != null)
			throw new UnsupportedOperationException();
		instance = this;
	}

	public static synchronized void subscribe(String type, ISubscriber handler) {
		if (handler == null) {
			throw new NullPointerException("handler 不能为空.");
		}
		if (!subscribers.containsKey(type)) {
			EventBus bus = new EventBus(type);
			subscribers.put(type, bus);
		} else {
			EventBus bus = (EventBus) subscribers.get(type);
			bus.register(handler);
		}
	}

	public static synchronized void publisher(String type, IEventEntry entry) {
		if (!subscribers.containsKey(type))
			return;
		EventBus bus = (EventBus) subscribers.get(type);
		bus.post(entry);
	}

	public static synchronized void unSubscribe(String type, ISubscriber handler) {
		if (!subscribers.containsKey(type))
			return;
		EventBus bus = (EventBus) subscribers.get(type);
		bus.unregister(handler);
	}

	@Deprecated
	public static boolean regist(Class eventType, IEventHandler<IEvent> handler) {
		if (instance == null)
			return false;
		instance.belleEventService.register(eventType, handler);
		return true;
	}

	@Deprecated
	public boolean remove(Class eventType, IEventHandler handler) {
		if (instance == null)
			return false;
		instance.belleEventService.remove(eventType, handler);
		return true;
	}

	@Deprecated
	public boolean exist(Class eventType) {
		if (instance == null)
			return false;
		return instance.belleEventService.exist(eventType);
	}
}
