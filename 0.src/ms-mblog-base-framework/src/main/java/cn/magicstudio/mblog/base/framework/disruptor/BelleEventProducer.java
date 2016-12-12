 package cn.magicstudio.mblog.base.framework.disruptor;
 
 
import cn.magicstudio.mblog.base.framework.event.BelleEvent;

import com.lmax.disruptor.RingBuffer;

public class BelleEventProducer {
	private final RingBuffer<BelleEvent> ringBuffer;

	public BelleEventProducer(RingBuffer<BelleEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public <TYPE extends Enum<TYPE>> void onData(TYPE type, Object data) {
		onData(type, data, null);
	}

	public <TYPE extends Enum<TYPE>> void onData(TYPE type, Object data,
			Object target) {
		long sequence = this.ringBuffer.next();
		try {
			BelleEvent event = (BelleEvent) this.ringBuffer.get(sequence);
			event.setType(type);
			event.setTarget(target);
			event.setData(data);
		} finally {
			this.ringBuffer.publish(sequence);
		}
	}
}
