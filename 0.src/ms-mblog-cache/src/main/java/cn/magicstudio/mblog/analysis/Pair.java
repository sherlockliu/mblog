package cn.magicstudio.mblog.analysis;

public class Pair<K> {
	public K key;
	public Double freq = Double.valueOf(0.0D);

	public Pair(K key, double freq) {
		this.key = key;
		this.freq = Double.valueOf(freq);
	}

	public String toString() {
		return "Candidate [key=" + this.key + ", freq=" + this.freq + "]";
	}
 }