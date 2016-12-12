package cn.magicstudio.mblog.base.framework.utils;

/**
 * @deprecated
 */
public class SequenceGenerator {
	protected static short COUNTER = 0;

	public static String generateSequence() {
		StringBuilder builder = new StringBuilder(20);
		builder.append(System.currentTimeMillis()).append(
				format(Short.valueOf(getCount())));
		return builder.toString();
	}

	protected static short getCount() {
		synchronized (SequenceGenerator.class) {
			if (COUNTER < 0) {
				COUNTER = 0;
			}
			return COUNTER++;
		}
	}

	protected static String format(Short shortValue) {
		String formatted = shortValue.toString();
		StringBuilder buf = new StringBuilder("00000");
		buf.replace(5 - formatted.length(), 5, formatted);
		return buf.toString();
	}

	public static void main(String[] args) {
		long advanceTimestamp = 26214400000L;
		long t = advanceTimestamp >> 18;

		System.out.println(t);
	}
}
