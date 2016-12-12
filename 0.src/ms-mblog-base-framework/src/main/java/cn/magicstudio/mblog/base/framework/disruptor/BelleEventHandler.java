package cn.magicstudio.mblog.base.framework.disruptor;


import cn.magicstudio.mblog.base.framework.event.BelleEvent;
import cn.magicstudio.mblog.base.framework.event.IEventDispatcher;

import com.lmax.disruptor.EventHandler;

public class BelleEventHandler implements EventHandler<BelleEvent> {
	private final IEventDispatcher dispatcher;

	public BelleEventHandler(IEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void onEvent(BelleEvent event, long sequence, boolean endOfBatch)
			throws Exception {
		this.dispatcher.dispatch(event);
	}
}
