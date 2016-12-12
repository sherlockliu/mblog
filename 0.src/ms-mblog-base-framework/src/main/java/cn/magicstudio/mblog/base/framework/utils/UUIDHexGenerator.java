package cn.magicstudio.mblog.base.framework.utils;

import cn.magicstudio.mblog.base.framework.uuid.UUID;

public class UUIDHexGenerator {
	public static String generate() {
		return new UUID().toString();
	}

	public static void main(String[] args) {
		String uuid = generate();
		System.out.println(uuid);
	}
}
