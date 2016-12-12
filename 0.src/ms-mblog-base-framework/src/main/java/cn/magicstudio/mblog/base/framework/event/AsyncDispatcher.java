package cn.magicstudio.mblog.base.framework.event;

import cn.magicstudio.mblog.base.framework.service.AbstractService;
import cn.magicstudio.mblog.base.framework.service.IService;
import cn.magicstudio.mblog.base.framework.event.IEvent;
import cn.magicstudio.mblog.base.framework.event.IEventDispatcher;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AsyncDispatcher extends AbstractService implements
		IEventDispatcher {
	private static final Log LOG = LogFactory.getLog(AsyncDispatcher.class);

	private Map<Class<?>, IEventHandler> eventDispatchers = null;

	public void init(Properties properties) {
		this.eventDispatchers = new ConcurrentHashMap();
		this.state = IService.STATE.INITED;
	}

	public void close() {
		this.eventDispatchers.clear();
		this.state = IService.STATE.STOPPED;
	}

	

	@Override
	public void register(Class<? extends Enum> eventType,
			IEventHandler<? extends IEvent> handler) {
		IEventHandler<? extends IEvent> registeredHandler = (IEventHandler) this.eventDispatchers
				.get(eventType);
		LOG.info("Registering " + eventType + " for " + handler.getClass());
		if (registeredHandler == null) {
			this.eventDispatchers.put(eventType, handler);
		} else if (!(registeredHandler instanceof MultiListenerHandler)) {
			MultiListenerHandler multiHandler = new MultiListenerHandler();
			multiHandler.addHandler(registeredHandler);
			multiHandler.addHandler(handler);
			this.eventDispatchers.put(eventType, multiHandler);
		} else {
			MultiListenerHandler multiHandler = (MultiListenerHandler) registeredHandler;
			multiHandler.addHandler(handler);
		}
		
	}

	public boolean exist(Class<? extends Enum> eventType) {
		IEventHandler<IEvent> registeredHandler = (IEventHandler) this.eventDispatchers
				.get(eventType);
		return registeredHandler != null;
	}
	

	@Override
	public void remove(Class<? extends Enum> eventType,
			IEventHandler<IEvent> handler) {
		IEventHandler<IEvent> registeredHandler = (IEventHandler) this.eventDispatchers
				.get(eventType);
		if (registeredHandler != null) {
			if ((registeredHandler instanceof MultiListenerHandler)) {
				MultiListenerHandler multiHandler = (MultiListenerHandler) registeredHandler;
				multiHandler.remove(handler);
				if (multiHandler.size() == 0)
					this.eventDispatchers.remove(eventType);
			} else {
				this.eventDispatchers.remove(eventType);
			}
		}
		
	}

	public void dispatch(IEvent event) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Dispatching the event " + event.getClass().getName()
					+ "." + event.toString());
		}
		Class<? extends Enum> type = event.getType().getDeclaringClass();
		try {
			IEventHandler handler = (IEventHandler) this.eventDispatchers
					.get(type);
			if (handler != null) {
				handler.handle(event);
			} else {
				LOG.error("No handler for registered for " + type);
			}
		} catch (Throwable t) {
			LOG.fatal("Error in dispatcher thread", t);
		}
	}

    static class MultiListenerHandler<T extends IEvent> implements IEventHandler {
		List<IEventHandler<T>> listofHandlers;

		public MultiListenerHandler() {
			this.listofHandlers = new CopyOnWriteArrayList();
		}

		public void handle(IEvent event) {
			for (IEventHandler<T> handler : this.listofHandlers) {
				handler.handle((T)event);
			}
		}

		void addHandler(IEventHandler<T> handler) {
			this.listofHandlers.add(handler);
		}

		void remove(IEventHandler<IEvent> handler) {
			this.listofHandlers.remove(handler);
		}

		int size() {
			return this.listofHandlers == null ? 0 : this.listofHandlers.size();
		}
	}

	public void clear() {
		close();
	}
}
