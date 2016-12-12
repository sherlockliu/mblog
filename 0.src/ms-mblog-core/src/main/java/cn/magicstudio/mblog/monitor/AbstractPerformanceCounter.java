package cn.magicstudio.mblog.monitor;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class AbstractPerformanceCounter implements PerformanceCounter {
	private String name;
	protected AtomicLong atomic = new AtomicLong(1L);
	protected AtomicLong count = new AtomicLong(0L);
	protected long min = 2000L;
	protected long max = 0L;
	protected long take = 0L;

	public AbstractPerformanceCounter(String name) {
		this.name = name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getName() {
		return this.name;
	}

	public long increment(long value) {
		if ((this.take == 0L) || (counting() == 0.0D))
			this.take = System.currentTimeMillis();
		long result = this.atomic.addAndGet(value);
		this.max = Math.max(value, this.max);
		this.min = Math.min(value, this.min);
		this.count.addAndGet(1L);
		return result;
	}

	public long total() {
		return this.count.get();
	}

	public long max() {
		return this.max;
	}

	public long min() {
		return this.min;
	}

	public double avg() {
		if (this.count.get() == 0L)
			return 0.0D;
		return this.atomic.get() / this.count.get();
	}

	private long increment = 0L;
	private double counting = 0.0D;

	public double counting() {
		long v = (System.currentTimeMillis() - this.take) / 1000L;
		if (v < 1L) {
			return this.counting;
		}
		long c = this.count.get();
		long r = c - this.increment;
		if (r <= 0L) {
			return this.counting;
		}
		this.take = System.currentTimeMillis();
		this.counting = (r / v);
		this.increment = c;
		return this.counting;
	}

	public synchronized void reset() {
		this.max = 0L;
		this.min = 2000L;
		this.count.set(0L);
		this.atomic.set(0L);
	}

	Counter info = new Counter();

	public Counter info() {
		this.info.avg = avg();
		this.info.max = max();
		this.info.name = this.name;
		this.info.counting = counting();
		this.info.total = total();
		this.info.min = this.min;
		return this.info;
	}

	public static class Counter implements Serializable {
		private static final long serialVersionUID = 6662244910096597486L;
		private int type;
		private String name;
		private long max;
		private long min;
		private long total;
		private double avg;
		private double counting;

		public int getType() {
			return this.type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getMax() {
			return this.max;
		}

		public void setMax(long max) {
			this.max = max;
		}

		public long getMin() {
			return this.min;
		}

		public void setMin(long min) {
			this.min = min;
		}

		public long getTotal() {
			return this.total;
		}

		public void setTotal(long total) {
			this.total = total;
		}

		public double getAvg() {
			return this.avg;
		}

		public void setAvg(double avg) {
			this.avg = avg;
		}

		public double getCounting() {
			return this.counting;
		}

		public void setCounting(double counting) {
			this.counting = counting;
		}
	}
}
