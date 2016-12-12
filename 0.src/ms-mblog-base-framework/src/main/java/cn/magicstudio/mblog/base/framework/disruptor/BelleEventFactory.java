package cn.magicstudio.mblog.base.framework.disruptor;


import cn.magicstudio.mblog.base.framework.event.BelleEvent;

import com.lmax.disruptor.EventFactory;

public class BelleEventFactory implements EventFactory<BelleEvent> {
	public BelleEvent newInstance() {
		return new BelleEvent();
	}
}
