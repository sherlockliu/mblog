package cn.magicstudio.mblog.core.event;

import com.google.common.eventbus.Subscribe;

public abstract interface ISubscriber {
	@Subscribe
	public abstract void listen(IEventEntry paramIEventEntry);
}
