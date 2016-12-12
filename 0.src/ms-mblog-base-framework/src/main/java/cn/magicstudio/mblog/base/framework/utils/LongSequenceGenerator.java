package cn.magicstudio.mblog.base.framework.utils;

import com.yougou.logistics.base.common.exception.FatalSequenceException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LongSequenceGenerator {
	private static final Log LOG = LogFactory
			.getLog(LongSequenceGenerator.class);

	private static long persistentTimestamp = 0L;

	private static final long advanceTimestamp = 2621440000L;

	private static long maxAssignedTimestamp;

	static {
		try {
			init();
		} catch (FatalSequenceException e) {
			LOG.error("LongSeqGenerator init error!", e);
			System.exit(0);
		}
	}

	private static long getTimestamps(int range) throws FatalSequenceException {
		long currentTime = System.currentTimeMillis() << 18;
		synchronized (LongSequenceGenerator.class) {

			if (currentTime > maxAssignedTimestamp) {
				maxAssignedTimestamp = currentTime + range - 1L;
			} else {
				maxAssignedTimestamp += range;
			}

			if (maxAssignedTimestamp >= persistentTimestamp) {
				long newPersistentTimestamp = maxAssignedTimestamp + 2621440000L;

				if (LOG.isDebugEnabled()) {
					LOG.debug("Try to sync set persistent timestamp "
							+ newPersistentTimestamp);
				}
				try {
					setPersistentTimestamp(newPersistentTimestamp);
				} catch (FatalSequenceException e) {
					LOG.fatal("Error to set timestamp", e);
					System.exit(0);
				}
			}

			if (maxAssignedTimestamp >= persistentTimestamp - 1.31072E9D) {
				long newPersistentTimestamp = persistentTimestamp + 2621440000L;
				if (LOG.isDebugEnabled()) {
					LOG.debug("Try to async set persistent timestamp "
							+ newPersistentTimestamp);
				}

				setPersistentTimestamp(newPersistentTimestamp);
			}

			return maxAssignedTimestamp - range + 1L;
		}
	}

	public static long getTimestamp() throws FatalSequenceException {
		return getTimestamps(1);
	}

	private static void init() throws FatalSequenceException {
		maxAssignedTimestamp = persistentTimestamp = 0L;
		long newPersistentTimestamp = maxAssignedTimestamp + 2621440000L;
		setPersistentTimestamp(newPersistentTimestamp);
	}

	private static void setPersistentTimestamp(long newTimestamp)
			throws FatalSequenceException {
		if (newTimestamp <= persistentTimestamp) {
			throw new FatalSequenceException(
					"Fatal error to set a smaller timestamp");
		}

		persistentTimestamp = newTimestamp;
	}

	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			int count = 20;
			for (int i = 0; i < count; i++) {
				System.out.println(getTimestamp());
			}

			long end = System.currentTimeMillis();
			long cost = end - start;
			System.out.println("cost:" + cost);

		} catch (FatalSequenceException e) {
			e.printStackTrace();
		}

		System.out.println(System.currentTimeMillis() << 18);
		System.out.println(Long.MAX_VALUE);

		System.out.println(getTime(persistentTimestamp));

		long currentTime = System.currentTimeMillis();

		long oneYear = 31536000000L;

		long maxYear = 35184372088831L;

		System.out.println(maxYear);
		System.out.println(currentTime);
		System.out.println(oneYear);

		long num = maxYear / oneYear;
		System.out.println("this Algorithm can't use about " + num + " years");
	}

	private static String getTime(long timeStamp) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		timeStamp >>= 18;
		Date date = new Date(timeStamp);
		return df.format(date);
	}
}
