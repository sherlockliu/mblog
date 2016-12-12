package cn.magicstudio.mblog.model;

import java.io.Serializable;

public class KeyValue<K, V> implements Serializable {
	private static final long serialVersionUID = 2891421287640327669L;
	private K key;
	private V value;

	public K getKey() {
		return (K) this.key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return (V) this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}
