package cn.magicstudio.mblog.base.framework.uuid;

import com.yougou.logistics.base.common.uuid.lang.Hex;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

public final class UUIDGen {
	private static AtomicLong lastTime = new AtomicLong(Long.MIN_VALUE);

	private static String macAddress = null;

//	private static long clockSeqAndNode = clockSeqAndNode
//			| (Math.random() * 16383.0D) << 48;

	public static long getClockSeqAndNode() {
//		return clockSeqAndNode;
		return 0;
	}

	public static long newTime() {
		return createTime(System.currentTimeMillis());
	}

	public static long createTime(long currentTimeMillis) {
		long timeMillis = currentTimeMillis * 10000L + 122192928000000000L;
		for (;;) {
			long current = lastTime.get();
			if (timeMillis > current) {
				if (lastTime.compareAndSet(current, timeMillis)) {
					break;
				}
			} else if (lastTime.compareAndSet(current, current + 1L)) {
				timeMillis = current + 1L;
				break;
			}
		}

		long time = timeMillis << 32;
//
//		time |= (timeMillis & 0xFFFF00000000) >> 16; TODO

		time |= 0x1000 | timeMillis >> 48 & 0xFFF;

		return time;
	}

	public static String getMACAddress() {
		return macAddress;
	}

	/* Error */
	static String getFirstLineOfCommand(String... commands)
			throws java.io.IOException {
		return "";
	}

	static class HardwareAddressLookup {
		public String toString() {
			String out = null;
			try {
				Enumeration<NetworkInterface> ifs = NetworkInterface
						.getNetworkInterfaces();
				if (ifs != null) {
					while (ifs.hasMoreElements()) {
						NetworkInterface iface = (NetworkInterface) ifs
								.nextElement();
						byte[] hardware = iface.getHardwareAddress();
						if ((hardware != null) && (hardware.length == 6)
								&& (hardware[1] != -1)) {
							out = Hex.append(new StringBuilder(36), hardware)
									.toString();
							break;
						}
					}
				}
			} catch (SocketException localSocketException) {
			}

			return out;
		}
	}
 }
