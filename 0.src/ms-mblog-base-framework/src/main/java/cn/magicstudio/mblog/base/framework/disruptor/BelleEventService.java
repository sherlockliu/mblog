package cn.magicstudio.mblog.base.framework.disruptor;

import cn.magicstudio.mblog.base.framework.event.AsyncDispatcher;
import cn.magicstudio.mblog.base.framework.event.BelleEvent;
import cn.magicstudio.mblog.base.framework.event.IEvent;
import cn.magicstudio.mblog.base.framework.event.IEventHandler;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.io.PrintStream;
import java.util.concurrent.Executor;


public class BelleEventService {
	private int bufferSize = 1024;

	private Executor executor;

	private String waitStrategy;

	private int producerType;

	private Disruptor<BelleEvent> disruptor;

	private BelleEventProducer producer;

	private AsyncDispatcher dispatcher;

	private static final int MAXIMUM_CAPACITY = 1073741824;

	public <TYPE extends Enum<TYPE>> void onData(TYPE type, Object data) {
		this.producer.onData(type, data);
	}

	public <TYPE extends Enum<TYPE>> void onData(TYPE type, Object data,
			Object target) {
		this.producer.onData(type, data, target);
	}

	public void register(Class<? extends Enum> eventType,
			IEventHandler<IEvent> handler) {
		this.dispatcher.register(eventType, handler);
	}

	public void remove(Class<? extends Enum> eventType,
			IEventHandler<IEvent> handler) {
		this.dispatcher.remove(eventType, handler);
	}

	public boolean exist(Class<? extends Enum> eventType) {
		return this.dispatcher.exist(eventType);
	}

	public void init() {
		BelleEventFactory factory = new BelleEventFactory();

		this.disruptor = new Disruptor(factory, this.bufferSize, this.executor,
				getProducerType(), getWaitStrategy());
		this.dispatcher = new AsyncDispatcher();
		this.dispatcher.init(null);

		this.disruptor
				.handleEventsWith(new EventHandler[] { new BelleEventHandler(
						this.dispatcher) });

		this.disruptor.start();

		RingBuffer<BelleEvent> ringBuffer = this.disruptor.getRingBuffer();
		this.producer = new BelleEventProducer(ringBuffer);
	}

	private WaitStrategy getWaitStrategy() {
		if (this.waitStrategy != null) {
			if (this.waitStrategy.equals("sleeping")) {
				return new SleepingWaitStrategy();
			}
			if (this.waitStrategy.equals("yielding")) {
				return new YieldingWaitStrategy();
			}
			if (this.waitStrategy.equals("busyspin")) {
				return new BusySpinWaitStrategy();
			}
		}
		return new BlockingWaitStrategy();
	}

	private ProducerType getProducerType() {
		if (this.producerType == 0)
			return ProducerType.SINGLE;
		return ProducerType.MULTI;
	}

	public void destroy() {
		this.dispatcher.clear();
		this.disruptor.shutdown();
	}

	public int getBufferSize() {
		return this.bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		if (bufferSize < 0) {
			this.bufferSize = 1024;
		} else if (bufferSize > 1073741824) {
			this.bufferSize = 1073741824;
		} else {
			int capacity = 1;
			while (capacity < bufferSize)
				capacity <<= 1;
			this.bufferSize = capacity;
		}
	}

	public Executor getExecutor() {
		return this.executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public void setWaitStrategy(String waitStrategy) {
		this.waitStrategy = waitStrategy;
	}

	public void setProducerType(int producerType) {
		this.producerType = producerType;
	}

	public static void main(String[] arg) throws InterruptedException {
		BelleEventService service = new BelleEventService();

		service.setBufferSize(1024);
		System.out.println(service.getBufferSize());
		System.out.println(Integer.MAX_VALUE);
	}

	public static enum EventType {
		Test, Test2;

		private EventType() {
		}
	}

	static class MyHandler implements IEventHandler<IEvent> {
		public void handle(IEvent event) {
			System.out.println(event);
		}
	}
}
